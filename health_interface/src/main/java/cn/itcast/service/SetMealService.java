package cn.itcast.service;

import cn.itcast.pojo.Setmeal;

public interface SetMealService {
    //1.新增套餐
    void add(Setmeal setmeal, Integer[] checkGroupIds);
}
