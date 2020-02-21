package cn.itcast.pojo;

import org.omg.PortableInterceptor.INACTIVE;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 报表实体类
 */
public class ReportData implements Serializable {
    private String reportDate;//报表时间
    private String thisWeekMonday;//本周第一天
    private String firstDay4ThisMonth;//本月第一天
    private Integer todayNewMember;//今天新增会员
    private Integer totalMember;//总会员数
    private Integer thisWeekNewMember;//本周新增会员数
    private Integer thisMonthNewMember;//本月新增会员数
    private Integer todayOrderNumber;//今日预约数
    private Integer todayVisitsNumber;//今日到诊数
    private Integer thisWeekOrderNumber;//本周预约数
    private Integer thisWeekVisitsNumber;//本周到诊数
    private Integer thisMonthOrderNumber;//本月预约数
    private Integer thisMonthVisitsNumber;//本月到诊数
    List<Map> hotSetmeal;//热门套餐

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getThisWeekMonday() {
        return thisWeekMonday;
    }

    public void setThisWeekMonday(String thisWeekMonday) {
        this.thisWeekMonday = thisWeekMonday;
    }

    public String getFirstDay4ThisMonth() {
        return firstDay4ThisMonth;
    }

    public void setFirstDay4ThisMonth(String firstDay4ThisMonth) {
        this.firstDay4ThisMonth = firstDay4ThisMonth;
    }

    public Integer getTodayNewMember() {
        return todayNewMember;
    }

    public void setTodayNewMember(Integer todayNewMember) {
        this.todayNewMember = todayNewMember;
    }

    public Integer getTotalMember() {
        return totalMember;
    }

    public void setTotalMember(Integer totalMember) {
        this.totalMember = totalMember;
    }

    public Integer getThisWeekNewMember() {
        return thisWeekNewMember;
    }

    public void setThisWeekNewMember(Integer thisWeekNewMember) {
        this.thisWeekNewMember = thisWeekNewMember;
    }

    public Integer getThisMonthNewMember() {
        return thisMonthNewMember;
    }

    public void setThisMonthNewMember(Integer thisMonthNewMember) {
        this.thisMonthNewMember = thisMonthNewMember;
    }

    public Integer getTodayOrderNumber() {
        return todayOrderNumber;
    }

    public void setTodayOrderNumber(Integer todayOrderNumber) {
        this.todayOrderNumber = todayOrderNumber;
    }

    public Integer getTodayVisitsNumber() {
        return todayVisitsNumber;
    }

    public void setTodayVisitsNumber(Integer todayVisitsNumber) {
        this.todayVisitsNumber = todayVisitsNumber;
    }

    public Integer getThisWeekOrderNumber() {
        return thisWeekOrderNumber;
    }

    public void setThisWeekOrderNumber(Integer thisWeekOrderNumber) {
        this.thisWeekOrderNumber = thisWeekOrderNumber;
    }

    public Integer getThisWeekVisitsNumber() {
        return thisWeekVisitsNumber;
    }

    public void setThisWeekVisitsNumber(Integer thisWeekVisitsNumber) {
        this.thisWeekVisitsNumber = thisWeekVisitsNumber;
    }

    public Integer getThisMonthOrderNumber() {
        return thisMonthOrderNumber;
    }

    public void setThisMonthOrderNumber(Integer thisMonthOrderNumber) {
        this.thisMonthOrderNumber = thisMonthOrderNumber;
    }

    public Integer getThisMonthVisitsNumber() {
        return thisMonthVisitsNumber;
    }

    public void setThisMonthVisitsNumber(Integer thisMonthVisitsNumber) {
        this.thisMonthVisitsNumber = thisMonthVisitsNumber;
    }

    public List<Map> getHotSetmeal() {
        return hotSetmeal;
    }

    public void setHotSetmeal(List<Map> hotSetmeal) {
        this.hotSetmeal = hotSetmeal;
    }
}
