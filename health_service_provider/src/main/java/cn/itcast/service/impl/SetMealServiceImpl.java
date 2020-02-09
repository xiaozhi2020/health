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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

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
        return MessageConstant.DELETE_SETMEAL_SUCCESS;
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
}
