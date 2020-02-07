package cn.itcast.service.impl;

import cn.itcast.constant.MessageConstant;
import cn.itcast.dao.CheckItemDao;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemServiceImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;
    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    /**
     * 检查项分页
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> checkItemPage = checkItemDao.selectByCondition(queryString);
        long total = checkItemPage.getTotal();
        List<CheckItem> result = checkItemPage.getResult();
        return new PageResult(total,result);
    }

    /**
     * 删除检查项
     * 会有RuntimeException异常但是Controller的try catch 获取不到,依然走try不走catch,
     * 所以要查中间表进行判端 然后 new RuntimeException 才会被try catch 监测到
     * @param id
     */
    @Override
    public String delete(Integer id) {
        Integer count = checkItemDao.findCountByCheckItemId(id);
        System.out.println(count);
        if (count>0){
            return MessageConstant.DELETE_CHECKItem_FAIL_BECAUSE_ASSOCIATION;
        }
        checkItemDao.delete(id);
        return null;
    }

    /**
     * 编辑检查项回显
     * @param id
     * @return
     */
    @Override
    public CheckItem findById(Integer id) {
        CheckItem checkItem= checkItemDao.findById(id);
        return checkItem;
    }

    /**
     * 编辑检查项
     * @param checkItem
     */
    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    /**
     * 查询所有检查项,因为增加检查组需要
     * @return
     */
    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }
}
