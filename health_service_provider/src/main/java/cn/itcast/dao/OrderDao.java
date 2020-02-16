package cn.itcast.dao;

import cn.itcast.pojo.Order;

import java.util.List;
import java.util.Map;

public interface OrderDao {
    //1.查询预约,根据会员id ,预约时间, 套餐id,来查询
    List<Order> findByCondition(Order order);
    //2.添加预约信息
    void add(Order order);
    //3.查询预约相关信息
    Map findByIdDetail(Integer id);
}
