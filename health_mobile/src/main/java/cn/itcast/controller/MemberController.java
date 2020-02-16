package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.constant.RedisMessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Member;
import cn.itcast.service.MemberService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RequestMapping("/member")
@RestController
public class MemberController {
    @Reference
    private MemberService memberService;
    @Autowired
    private JedisPool jedisPool;
    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response){
        //1.获取页面传来的参数
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        //2.获取Redis保存的验证码
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCode!=null&&validateCodeRedis!=null&&validateCode.equals(validateCodeRedis)){
            //验证码正确
           Member member= memberService.findByTelephone(telephone);
           if (member==null){
               member = new Member();//防止空指针异常;因为此刻为null情况下,并且没有此语句,用set,get会宝空指针异常
               member.setPhoneNumber(telephone);
               member.setRegTime(new Date());
               memberService.add(member);

           }
            //登录成功
            //写入Cookie，跟踪用户
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            //保存会员信息到Redis中
            String json = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }

    }
}
