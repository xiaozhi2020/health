package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.service.MemberService;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;

    /**
     * 会员数量折线统计图
     * @return
     */
    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){
        try {
            Map<String,Object> map = new HashMap<>();
            List<String> months = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MONTH,-12);
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH,1);
                Date calendarTime = calendar.getTime();
                months.add(new SimpleDateFormat("yyyy.MM").format(calendarTime));
            }
            map.put("months",months);
            List<Integer> memberCount = memberService.findMemberCountByMonths(months);
            map.put("memberCount",memberCount);
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

   /* public static void main(String[] args) {
        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            Date calendarTime = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(calendarTime));
            System.out.println(months);
        }
    }*/

    /**
     * 套餐预约占比饼形图
     * @return
     */
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){
       try {
           Map<String,Object> map =new HashMap<>();
           List<String> setmealNames= new ArrayList<>();
           List<Map<String,Object>> setmealCount =setMealService.findSetmealCount();
           for (Map<String, Object> sc : setmealCount) {
               String setmealName = (String) sc.get("name");
               setmealNames.add(setmealName);
           }
           map.put("setmealNames",setmealNames);
           map.put("setmealCount",setmealCount);
           return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
       } catch (Exception e) {
           e.printStackTrace();
           return new Result(false, MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
       }
   }
}
