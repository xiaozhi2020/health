package cn.itcast.service;

import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    //1.新增检查组
    void add(CheckGroup checkGroup, Integer[] checkItemIds);
    //2.检查组分页
    PageResult findPage(QueryPageBean queryPageBean);
    //3.编辑检查组回显检查项
    CheckGroup findById(Integer id);
    //3.1上面执行成功时候,前端才会发送此请求 然后返回的关联项的id
    List<Integer> findCheckItemIdsByCheckGroupId(Integer id);
    //4.编辑检查组
    void edit(CheckGroup checkGroup, Integer[] checkItemIds);
    //5.删除检查组
    String delete(Integer id);
}
