package cn.itcast.service.impl;

import cn.itcast.constant.MessageConstant;
import cn.itcast.dao.OrderSettingDao;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderMessage;
import cn.itcast.pojo.OrderSetting;
import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.service.OrderService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    /**
     * 体检预约
     * @param orderMessage
     * @return
     */
    @Override
    public Result order(OrderMessage orderMessage) {
        //1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
        String orderDate = orderMessage.getOrderDate();
        OrderSetting orderSetting=orderSettingDao.findByOrderDate(orderDate);
        if (orderSetting==null){
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//可预约人数
        if (reservations>=number){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        Member member= memberDao.findByTelephone(orderMessage.getTelephone());
        if (member!=null){
            Integer memberId = member.getId();//会员id_查数据库的
            String picktime = orderMessage.getOrderDate();//预约时间_页面传过来的
            Integer setmealId = orderMessage.getSetmealId();//套餐id_页面传过来的
            //设值
            Order order = new Order();
            order.setMemberId(memberId);
            try {
                order.setOrderDate(DateUtils.parseString2Date(picktime));
            } catch (Exception e) {
                e.printStackTrace();
            }
            order.setSetmealId(setmealId);
            //查询预约
            List<Order> orderList= orderDao.findByCondition(order);
            if (orderList!=null){
                //用户重复预约
                return  new Result(false,MessageConstant.HAS_ORDERED);
            }

        } else {
            //4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member = new Member();//防止空指针异常
            member.setName(orderMessage.getName());
            member.setSex(orderMessage.getSex());
            member.setIdCard(orderMessage.getIdCard());
            member.setPhoneNumber(orderMessage.getTelephone());
            try {
                member.setRegTime(DateUtils.parseString2Date(orderMessage.getOrderDate()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            memberDao.add(member);//自动完成会员注册:xml文件要获取主键id,不然预约成功设置主键没有值
        }
        //5、预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());//设置会员ID
        try {
            order.setOrderDate(DateUtils.parseString2Date(orderMessage.getOrderDate()));//预约日期
        } catch (Exception e) {
            e.printStackTrace();
        }
        order.setOrderType(orderMessage.getOrderType());//预约类型
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(orderMessage.getSetmealId());//套餐ID
        orderDao.add(order);//添加预约信息:xml文件要获取主键id,不然预约成功设置主键没有值

        //设置已预约人数+1
        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS,order.getId());
    }

    /**
     * 查询预约相关信息(体检人姓名,预约日期,套餐名称,预约类型)
     * @param id
     * @return
     */
    @Override
    public Map findById(Integer id) {
        Map map = orderDao.findByIdDetail(id);
        return map ;
    }
}
