package cn.itcast.service.impl;

import cn.itcast.dao.MemberDao;
import cn.itcast.dao.OrderDao;
import cn.itcast.pojo.ReportData;
import cn.itcast.service.ReportService;
import cn.itcast.utils.DateUtils;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public ReportData getBusinessReportData() {
        try {
            ReportData reportData = new ReportData();
            //1.设置报表时间
            String today = DateUtils.parseDate2String(DateUtils.getToday());
            reportData.setReportDate(today);
            //2.获得本周一日期//无需放在实体类
            String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());
            reportData.setThisWeekMonday(thisWeekMonday);
            //3.获得本月第一天日期//无需放在实体类
            String firstDay4ThisMonth = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());
            reportData.setFirstDay4ThisMonth(firstDay4ThisMonth);
            

            //4.总会员数
            Integer totalMember = memberDao.findMemberTotalCount();
            reportData.setTotalMember(totalMember);
            //5.本日新增会员数
            Integer todayNewMember = memberDao.findMemberCountByDate(today);
            reportData.setTodayNewMember(todayNewMember);
            //6.本周新增会员数
            Integer thisWeekNewMember =memberDao.findMemberCountAfterDate(thisWeekMonday);
            reportData.setThisWeekNewMember(thisWeekNewMember);
            //7.本月新增会员数
            Integer thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDay4ThisMonth);
            reportData.setThisMonthNewMember(thisMonthNewMember);
            //8.今日预约数
            Integer todayOrderNumber = orderDao.findOrderCountByDate(today);
            reportData.setTodayOrderNumber(todayOrderNumber);
            //9.本周预约数
            Integer thisWeekOrderNumber = orderDao.findOrderCountAfterDate(thisWeekMonday);
            reportData.setThisWeekOrderNumber(thisWeekOrderNumber);
            //10.本月预约数
            Integer thisMonthOrderNumber = orderDao.findOrderCountAfterDate(firstDay4ThisMonth);
            reportData.setThisMonthOrderNumber(thisMonthOrderNumber);


            //11.今日到诊数
            Integer todayVisitsNumber = orderDao.findVisitsCountByDate(today);
            reportData.setTodayVisitsNumber(todayVisitsNumber);
            //12.本周到诊数
            Integer thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(thisWeekMonday);
            reportData.setThisWeekVisitsNumber(thisWeekVisitsNumber);
            //13.本月到诊数
            Integer thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDay4ThisMonth);
            reportData.setThisMonthVisitsNumber(thisMonthVisitsNumber);

            //14.热门套餐查询
            List<Map> hotSetmeal = orderDao.findHotSetmeal();
            reportData.setHotSetmeal(hotSetmeal);
            return reportData;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
}
