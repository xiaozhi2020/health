package cn.itcast.service.impl;


import cn.itcast.dao.CheckGroupDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckGroup;
import cn.itcast.service.CheckGroupService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {
    @Autowired
    private CheckGroupDao checkGroupDao;
    /**
     * 新增检查组:还要在中间表增加关联;所以增加时要额外获取主键
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkItemIds) {
        checkGroupDao.add(checkGroup);
        setCheckGroupAndCheckItem(checkGroup,checkItemIds);
    }

    /**
     * 检查组分页
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> checkGroupPage = checkGroupDao.findPage(queryString);
        long total = checkGroupPage.getTotal();
        List<CheckGroup> result = checkGroupPage.getResult();
        return new PageResult(total,result);
    }

    /**
     * 编辑检查组回显检查项
     * @param id
     * @return
     */
    @Override
    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    /**
     * 上面执行成功时候,前端才会发送此请求 然后返回的关联项的id
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    /**
     * 编辑检查组
     * @param checkGroup
     * @param checkItemIds
     */
    @Override
    public void edit(CheckGroup checkGroup, Integer[] checkItemIds) {
        //1.修改信息
        checkGroupDao.edit(checkGroup);
        //2.清除与检查项的关联
        checkGroupDao.deleteCheckItemAssocication(checkGroup.getId());
        //3.重新建立关联:与add不同的是这是有id的,而add需要在xml中设置
        setCheckGroupAndCheckItem(checkGroup,checkItemIds);
    }

    /**
     * 删除检查组:是只看检查项还是只看套餐,还是都要看
     * @param id
     * @return
     */
    @Override
    public String delete(Integer id) {
        //1.查看有无关联检查项
        int checkItemCount = checkGroupDao.findCheckItemCount(id);
        //2.查看有无关联套装
        int setmealCount = checkGroupDao.findSetmeslCount(id);
        if (checkItemCount>0 || setmealCount>0){
            return null;
        }
        //删除
        checkGroupDao.delete(id);
        return "无关联:待优化的逻辑";
    }

    /**
     * 查询所有检查组,套餐要显示
     * @return
     */
    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    /**
     * 封装检查组与检查项多对多的关系
     * @param checkGroup
     * @param checkItemIds
     */
    public void setCheckGroupAndCheckItem(CheckGroup checkGroup, Integer[] checkItemIds){
        Integer checkGroupId = checkGroup.getId();
        for (Integer checkItemId : checkItemIds) {
            Map<String,Integer> map = new HashMap<>();
            map.put("checkGroupId",checkGroupId);
            map.put("checkItemId",checkItemId);
            checkGroupDao.setCheckGroupAndCheckItem(map);
        }
    }
}
