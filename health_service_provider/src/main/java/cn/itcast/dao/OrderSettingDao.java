package cn.itcast.dao;

import cn.itcast.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface OrderSettingDao {

    //1.1先查询数据
    public long findCountByOrderDate(Date orderDate);
    //1.2没数据增加
    public void add(OrderSetting orderSetting);
    //1.3有数据修改
    public void editNumberByOrderDate(OrderSetting orderSetting);
    //2获取批量导入数据
    List<OrderSetting> getOrderSettingByMonth(Map map);
    //3.预约设置:根据时间查询有无预约设置
    OrderSetting findByOrderDate(String orderDate);
    //4.预约时添加已预约的次数
    void editReservationsByOrderDate(OrderSetting orderSetting);
}
