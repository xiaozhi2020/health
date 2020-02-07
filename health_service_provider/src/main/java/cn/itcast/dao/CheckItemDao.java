package cn.itcast.dao;

import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckItemDao {
    //1.新增检查项
    void add(CheckItem checkItem);
    //2.检查项分页
    Page<CheckItem> selectByCondition(String queryString);
    //3.1删除前查询关联
    int findCountByCheckItemId(Integer id);
    //3.2删除检查项
    void delete(Integer id);
    //4编辑检查项回显
    CheckItem findById(Integer id);
    //5.编辑检查项
    void edit(CheckItem checkItem);
    //6.查询所有检查项,因为增加检查组需要
    List<CheckItem> findAll();
}
