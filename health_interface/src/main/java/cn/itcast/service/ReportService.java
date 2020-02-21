package cn.itcast.service;

import cn.itcast.pojo.ReportData;

import java.util.List;

public interface ReportService {
    //1.运营数据统计
    ReportData getBusinessReportData();
}
