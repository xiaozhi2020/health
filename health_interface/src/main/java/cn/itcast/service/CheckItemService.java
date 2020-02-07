package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckItem;

import java.util.List;

public interface CheckItemService {

    //1.新增检查项
    void add(CheckItem checkItem);
    //2.检查项分页
    PageResult findPage(QueryPageBean queryPageBean);
    //3.删除检查项
    String delete(Integer id);
    //4.编辑检查项回显
    CheckItem findById(Integer id);
    //5.编辑检查项
    void edit(CheckItem checkItem);
    //6.查询所有检查项,因为增加检查组需要
    List<CheckItem> findAll();
}
