package cn.itcast.controller;


import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {

    @Reference
    private SetMealService setMealService;

    /**
     * 获取所有套餐
     * @return
     */
    @RequestMapping("/getSetMeal")
    public Result getSetMeal(){
        try {
            List<Setmeal> setMealList= setMealService.getSetMeal();
            return new Result(true,MessageConstant.GET_SETMEAL_LIST_SUCCESS,setMealList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_SETMEAL_LIST_FAIL);
        }

    }

    /**
     * 分页
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
       return setMealService.findPage(queryPageBean);
    }

    /**
     * 套餐详情的查询
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Setmeal setmeal= setMealService.findDetailById(id);
            return  new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

}
