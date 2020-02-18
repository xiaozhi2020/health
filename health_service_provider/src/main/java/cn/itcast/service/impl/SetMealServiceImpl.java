package cn.itcast.service.impl;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisConstant;
import cn.itcast.dao.SetMealDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;
    @Autowired
    private JedisPool jedisPool;
    @Value("${out_put_path}")
    private String outPutPath;//从属性文件中读取要生成的html对应的目录
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 新增套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealDao.add(setmeal);
        setSetMealAndCheckGroup(setmeal,checkGroupIds);
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //生成静态页面
        generateMobileStaticHtml();
    }

    /**
     * 套餐分页查询
     * @return
     * @param queryPageBean
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<Setmeal> setmealPage = setMealDao.findPage(queryString);
        List<Setmeal> result = setmealPage.getResult();
        long total = setmealPage.getTotal();
        return new PageResult(total,result);
    }

    /**
     * 编辑套餐回显基本信息
     * @param id
     * @return
     */
    @Override
    public Setmeal findById(Integer id) {

        return setMealDao.findById(id);

    }

    /**
     * 编辑套餐回显已被选中的检查组
     * @param id
     * @return
     */
    @Override
    public Integer[] findCheckGroupIdsBySetMealId(Integer id) {
        return setMealDao.findCheckGroupIdsBySetMealId(id);
    }

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void edit(Setmeal setmeal, Integer[] checkGroupIds) {
        //编辑
        setMealDao.edit(setmeal);
        //删除与检查组关联
        setMealDao.deleteAssociation(setmeal.getId());
        //重建与检查组关联
        setSetMealAndCheckGroup(setmeal,checkGroupIds);

        /**解决bug的两步:
         * 小志:
         * 编辑时上传了新图片 新增存入数据库的图片,也就变成了垃圾图片 但它还在redis的小集合中,和大集合比对不出来...
         *
         * 小志:
         * 这个bug怎么解决
         *
         * 小志:
         * 我编辑之后原来的图片的名字就被双向绑定给替换了
         *
         * 小志:
         * 我就获取不了旧图片的名字,然后就不能去小集合里面把他删了*/
        //第一步:获取回显时的对象,通过他获取回显的图片名字,然后去小集合里面删掉
        Setmeal oldSetMeal = setMealDao.findById(setmeal.getId());
        //第二部:删除这个对象存入小集合的旧的图片名字
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,oldSetMeal.getImg());
        //存入Redis小集合
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        generateMobileStaticHtml();
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @Override
    public String delete(Integer id) {
        Integer count =setMealDao.findCheckGroupCountBySetMealId(id);
        if (count>0){
            return null;
        }
        setMealDao.delete(id);
        //第一步:获取回显时的对象,通过他获取回显的图片名字,然后去小集合里面删掉
        Setmeal oldSetMeal = setMealDao.findById(id);
        //第二部:删除这个对象存入小集合的旧的图片名字
        jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES,oldSetMeal.getImg());
        generateMobileStaticHtml();
        //删除后要将对应生成的静态文件一起删除
        File file = new File(outPutPath + "/" + "setmeal_detail_" + id + ".html");
        if (file.exists()){
            file.delete();
        }
        //其实还要判断页数,假如你把这个页面的最后一条数据删了,那么这个页面也要删
        return MessageConstant.DELETE_SETMEAL_SUCCESS;
    }

    /**
     * 获取所有套餐,手机端需求
     * @return
     */
    @Override
    public List<Setmeal> getSetMeal() {
        return setMealDao.getSetMeal();
    }

    /**
     * 手机端套餐详情查询
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(Integer id) {
        return setMealDao.findDetailById(id);
    }

    /**
     * 套餐预约占比饼形图,根据套餐查询预约
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        List<Map<String, Object>> mapList= setMealDao.findSetmealCount();
        return mapList;
    }

    /**
     * 封装新增套餐关联检查组
     * @param setmeal
     * @param checkGroupIds
     */
    public void setSetMealAndCheckGroup(Setmeal setmeal, Integer[] checkGroupIds){
        Integer setMealId = setmeal.getId();
        for (Integer checkGroupId : checkGroupIds) {
            Map<String,Integer> map= new HashMap<>();
            map.put("setMealId",setMealId);
            map.put("checkGroupId",checkGroupId);
            setMealDao.setSetMealAndCheckGroup(map);
        }
    }

    /**
     * 生成当前方法所需的静态页面
     */
    public void generateMobileStaticHtml(){
        //在生成静态页面之前需要查询数据
        Integer user_total= setMealDao.findAllCount();
        int user_pageSize=4;
        int currentPage=(int)Math.ceil(user_total*1.0/user_pageSize);//遍历页码,一次性全生成
        for (int user_currentPage = 1; user_currentPage <= currentPage; user_currentPage++) {
            PageHelper.startPage(user_currentPage,user_pageSize);
            Page<Setmeal> page = setMealDao.findPage(null);/*设置无条件*/
            List<Setmeal> result = page.getResult();
            if (page != null && page.size() > 0){
                //需要生成套餐列表静态页面
                generateMobileSetmealListHtml(result,user_currentPage,user_total);
                //需要生成套餐详情静态页面
                generateMobileSetmealDetailHtml(result);
            }
        }


    }

    /**
     * 生成套餐列表静态页面
     * @param list
     */
    public void generateMobileSetmealListHtml(List<Setmeal> list,Integer user_currentPages,Integer user_total){
        Map map = new HashMap();
        //为模板提供数据，用于生成静态页面
        map.put("setmealList",list);
        map.put("total",user_total);
        map.put("currentPage",user_currentPages);
        generteHtml("mobile_setmeal.ftl","m_setmeal_"+user_currentPages  +".html",map);

    }

    /**
     * 生成套餐详情静态页面（可能有多个）
     * @param list
     */
    public void generateMobileSetmealDetailHtml(List<Setmeal> list){
        for (Setmeal setmeal : list) {
            Map map = new HashMap();
            map.put("setmeal",setMealDao.findDetailById(setmeal.getId()));
            generteHtml("mobile_setmeal_detail.ftl","setmeal_detail_" + setmeal.getId() + ".html",map);
        }
    }

    /**
     * 通用的方法，用于生成静态页面
     * @param templateName
     * @param htmlPageName
     * @param map
     */
    public void generteHtml(String templateName,String htmlPageName,Map map){
        Configuration configuration = freeMarkerConfigurer.getConfiguration();//获得配置对象
        Writer out = null;
        try {
            Template template = configuration.getTemplate(templateName);
            //构造输出流
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPutPath + "/" + htmlPageName),"UTF-8"));
            //outPutPath + "/" + htmlPageName 不加"/",那么配置文件要多加/
            //输出文件
            template.process(map,out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
