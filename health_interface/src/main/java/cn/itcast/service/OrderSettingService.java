package cn.itcast.service;

import cn.itcast.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {
    //Excel批量上传
    void add(ArrayList<OrderSetting> data);
    //获取已经设置数据
    List<Map> getOrderSettingByMonth(String date);
    //可预约人数设置
    void editNumberByDate(OrderSetting orderSetting);
}
