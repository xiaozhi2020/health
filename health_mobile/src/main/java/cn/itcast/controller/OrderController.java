package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Order;
import cn.itcast.pojo.OrderMessage;
import cn.itcast.service.OrderService;
import cn.itcast.utils.SMSUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RequestMapping("/order")
@RestController
public class OrderController {
    @Reference
    private OrderService orderService;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/submit")
    /**
     * 提交预约信息
     */
    public Result submit(@RequestBody OrderMessage orderMessage){
        //从Redis里面获得保存的手机号和验证码
        String validateCodeRedis = jedisPool.getResource().get(orderMessage.getTelephone() + RedisMessageConstant.SENDTYPE_ORDER);
        String telephoneRedis = jedisPool.getResource().get("telephone" + orderMessage.getTelephone() + RedisMessageConstant.SENDTYPE_ORDER);
        //将用户输入的验证码/手机号和对应存入Redis的做对比
        String validateCode = orderMessage.getValidateCode();
        String telephone = orderMessage.getTelephone();
        //验证码对比失败
        if (validateCodeRedis==null||validateCodeRedis.length()==0 || validateCode==null||validateCode.length()==0||!validateCode.equals(validateCodeRedis)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //手机号失败
        if (telephoneRedis==null||telephoneRedis.length()==0||telephone==null||telephone.length()==0||!telephone.equals(telephoneRedis)){
            return new Result(false, MessageConstant.VALIDATETELEPHONE_ERROR);
        }
        //对比成功,调用服务完成预约业务处理
        //主要加强判断
        if (validateCode.equals(validateCodeRedis)&&telephone.equals(telephoneRedis)){
            orderMessage.setOrderType(Order.ORDERTYPE_WEIXIN);//设置预约类型,分微信和电话,此处是电话
            Result result= null;
            try {
                //体检预约
                result = orderService.order(orderMessage);//调用dubbo的服务要try catch处理
            } catch (Exception e) {
                e.printStackTrace();
                return result;

            }
            if (result.isFlag()){
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,orderMessage.getOrderDate());
                } catch (ClientException e) {
                    e.printStackTrace();
                }

            }
            return result;
        }
        //最终都不走还是属于不成功,返回页面信息(再一次加强判断)
        return new Result(false,MessageConstant.VALIDATETELEPHONE_ERROR+"或者"+MessageConstant.VALIDATECODE_ERROR);
    }

    /**
     * 查询预约相关信息
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return  new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }

    }
}
