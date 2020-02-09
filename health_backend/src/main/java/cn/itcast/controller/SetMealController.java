package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import cn.itcast.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Reference
    private SetMealService setMealService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 1.文件上传
     * @param multipartFile
     * @return
     */
    /*@RequestParam("imgFile")通过该注释拿到文件对象,如果不想用该注释那么需要将形成名设置成imgFile,也就是与前端一直,而不是multipartFile;视频老师讲的有误*/
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile){
        try {//获取文件后缀
            String originalFilename = multipartFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            String fileType = originalFilename.substring(lastIndexOf - 1);

            //自定义文件名
            String fileName=UUID.randomUUID().toString()+fileType;
            try {
                QiniuUtils.upload2Qiniu(multipartFile.getBytes(),fileName);

                //存入Redis的大集合
                jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
            }
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 2.新增套餐
     * @param setmeal
     * @param checkGroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setMealService.add(setmeal,checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
           return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 3.套餐分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
       return setMealService.findPage(queryPageBean);
    }

    /**
     * 编辑套餐回显基本信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal = setMealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 编辑套餐回显已选中的检查组
     * @param id
     * @return
     */
    @RequestMapping("/findCheckGroupIdsBySetMealId")
    public Result findCheckGroupIdsBySetMealId(Integer id) {
        try {
            //可以这个List<Integer>可以Integer[]
            Integer[] ids = setMealService.findCheckGroupIdsBySetMealId(id);
            return new Result(true, MessageConstant.QUERY_SETMEALLIST_SUCCESS, ids);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }

    /**
     * 编辑套餐
     * @param setmeal
     * @param checkGroupIds
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody Setmeal setmeal,Integer[] checkGroupIds){
        try {
            setMealService.edit(setmeal,checkGroupIds);
            return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.EDIT_SETMEAL_FAIL);
        }
    }

    /**
     * 删除套餐
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
           String str = setMealService.delete(id);
           if (str==null){
              return new Result(false,MessageConstant.DELETE_SETMEAL_FAIL_BECAUSE_ASSOCIATION);
           }
           return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
           return new Result(true,MessageConstant.DELETE_SETMEAL_FAIL);
        }

    }
}
