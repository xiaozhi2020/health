package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Setmeal;

public interface SetMealService {
    //1.新增套餐
    void add(Setmeal setmeal, Integer[] checkGroupIds);
    //2.套餐分页查询
    PageResult findPage(QueryPageBean queryPageBean);
    //3.1编辑套餐回显基本信息
    Setmeal findById(Integer id);
    //3.2编辑套餐回显被选择的检查组
    Integer[] findCheckGroupIdsBySetMealId(Integer id);
    //4编辑套餐
    void edit(Setmeal setmeal, Integer[] checkGroupIds);
    //5.删除套餐
    String delete(Integer id);
}
