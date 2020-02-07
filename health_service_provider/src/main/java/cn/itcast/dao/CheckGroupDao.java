package cn.itcast.dao;

import cn.itcast.pojo.CheckGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {
    //1.1新增检查组
    void add(CheckGroup checkGroup);
    //1.2新增检查组时关联检查项
    void setCheckGroupAndCheckItem(Map<String, Integer> map);
    //2.检查组组分页
    Page<CheckGroup> findPage(String queryString);
    //3.编辑的回显检查项
    CheckGroup findById(Integer id);
    //3.1上面执行成功时候,前端才会发送此请求 然后返回的关联项的id
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    //4.1修改信息
    void edit(CheckGroup checkGroup);
    //4.2清除与检查项的关联
    void deleteCheckItemAssocication(Integer id);
    //5.1查看有无关联检查项
    int findCheckItemCount(Integer id);
    //5.2查看有无关联套装
    int findSetmeslCount(Integer id);
    //5.3删除检查组
    void delete(Integer id);
}
