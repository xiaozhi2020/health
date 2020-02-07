package cn.itcast.service;

import cn.itcast.dao.CheckItemDao;
import cn.itcast.pojo.CheckItem;
import com.alibaba.dubbo.config.annotation.Service;


@Service(interfaceClass = CheckItemService.class)
public class CheckItemServiceImpl implements CheckItemService {

    private CheckItemDao checkItemDao;
    /**
     * 新增检查项
     * @param checkItem
     */
    @Override
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }
}
