package cn.itcast.dao;

import cn.itcast.pojo.Setmeal;

import java.util.Map;

public interface SetMealDao {
    //1.1新增套餐
    void add(Setmeal setmeal);
    //1.2新增套餐关联检查组
    void setSetmealAndCheckGroup(Map<String, Integer> map);

}
