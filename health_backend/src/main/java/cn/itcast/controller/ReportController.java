package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.ReportData;
import cn.itcast.service.MemberService;
import cn.itcast.service.ReportService;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private MemberService memberService;
    @Reference
    private SetMealService setMealService;
    @Reference
    private ReportService reportService;

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

    /**
     * 运营数据统计
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            ReportData reportData = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            ReportData reportDataObject = reportService.getBusinessReportData();
            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = reportDataObject.getReportDate();
            Integer todayNewMember = reportDataObject.getTodayNewMember();
            Integer totalMember = reportDataObject.getTotalMember();
            Integer thisWeekNewMember = reportDataObject.getThisWeekNewMember();
            Integer thisMonthNewMember = reportDataObject.getThisMonthNewMember();
            Integer todayOrderNumber = reportDataObject.getTodayOrderNumber();
            Integer thisWeekOrderNumber = reportDataObject.getThisWeekOrderNumber();
            Integer thisMonthOrderNumber = reportDataObject.getThisMonthOrderNumber();
            Integer todayVisitsNumber = reportDataObject.getTodayVisitsNumber();
            Integer thisWeekVisitsNumber = reportDataObject.getThisWeekVisitsNumber();
            Integer thisMonthVisitsNumber = reportDataObject.getThisMonthVisitsNumber();
            List<Map> hotSetmeal = reportDataObject.getHotSetmeal();
            String filePath= request.getSession().getServletContext().getRealPath("template")+ File.separator+ "report_template.xlsx";
            //基于提供的Excel模板文件在内存中创建一个Excel表格对象
            XSSFWorkbook xssfWorkbook =new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //读取第一个工作表
            XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期
            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数
            int rowNum=13;
            for (Map map : hotSetmeal) {
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比

            }

            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");//代表的是Excel文件类型
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");//指定以附件形式进行下载
            xssfWorkbook.write(outputStream);

            outputStream.flush();
            outputStream.close();
            outputStream.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
