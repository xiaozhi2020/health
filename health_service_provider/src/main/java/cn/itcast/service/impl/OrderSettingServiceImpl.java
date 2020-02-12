package cn.itcast.service.impl;

import cn.itcast.dao.OrderSettingDao;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.service.OrderSettingService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    /**
     * 批量导入预约设计
     * @param data
     */
    @Override
    public void add(ArrayList<OrderSetting> data) {
       if (data!=null&&data.size()>0){
           for (OrderSetting orderSetting : data) {
               long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
               /*如果返回对象,贼三种情况,对比时间和预约人数,都相等continue,不做操作,减少数据库消耗
               * 时间相同和预约人数不同,修改
               * 都不相等,增加*/
               if (count>0){
                   orderSettingDao.editNumberByOrderDate(orderSetting);

               }else {
                   orderSettingDao.add(orderSetting);
               }
           }
       }
    }

    /**
     * 获取批量导入数据回显
     * @param date
     * @return
     */
    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //模糊查询和老师的设置可不设置开始结束然后放入map集合 作为参数,这里就没用视频老师方法但是注意月份带0和不带0
        String begin = date+"-01";
        String end=date+"-31";
        Map<String,String> m = new HashMap();
        m.put("begin",begin);
        m.put("end",end);



        List<OrderSetting> orderSettingList = orderSettingDao.getOrderSettingByMonth(m);
        List<Map> mapResult = new ArrayList<>();
        for (OrderSetting orderSetting : orderSettingList) {
            Map<String,Object> map = new HashMap<>();
            map.put("date",orderSetting.getOrderDate().getDate());//获取几号
            map.put("number",orderSetting.getNumber());
            map.put("reservations",orderSetting.getReservations());
            mapResult.add(map);
        }
        return mapResult;
    }

    /**
     * 可预约人数设置
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        //根据时间查询是否已经设置了预约人数
        long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count>0){
            //有,走编辑
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //无,走添加
            orderSettingDao.add(orderSetting);
        }
    }
}
