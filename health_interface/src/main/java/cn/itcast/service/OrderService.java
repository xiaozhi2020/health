package cn.itcast.service;

import cn.itcast.entity.Result;
import cn.itcast.pojo.OrderMessage;

import java.util.Map;

public interface OrderService {
    //1.体检预约
    Result order(OrderMessage orderMessage);
    //2.查询预约相关信息
    Map findById(Integer id);
}
