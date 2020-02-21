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
    //4.查询当日预约数量
    Integer findOrderCountByDate(String today);
    //5.参数不同查询不同,本周/本月预约
    Integer findOrderCountAfterDate(String firstDay4ThisMonth);
    //6.本日到诊数
    Integer findVisitsCountByDate(String today);
    //7.本周本月到诊数
    Integer findVisitsCountAfterDate(String thisWeekMonday);
    //8.热门套餐查询
    List<Map> findHotSetmeal();

}
