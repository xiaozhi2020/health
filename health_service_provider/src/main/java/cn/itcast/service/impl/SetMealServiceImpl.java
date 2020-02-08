package cn.itcast.service.impl;

import cn.itcast.dao.SetMealDao;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = SetMealService.class)
@Transactional
public class SetMealServiceImpl implements SetMealService {
    @Autowired
    private SetMealDao setMealDao;

    /**
     * 新增套餐
     * @param setmeal
     * @param checkGroupIds
     */
    @Override
    public void add(Setmeal setmeal, Integer[] checkGroupIds) {
        setMealDao.add(setmeal);
        setSetMealAndCheckGroup(setmeal,checkGroupIds);
    }

    /**
     * 封装新增套餐关联检查组
     * @param setmeal
     * @param checkGroupIds
     */
    public void setSetMealAndCheckGroup(Setmeal setmeal, Integer[] checkGroupIds){
        Integer setMealId = setmeal.getId();
        for (Integer checkGroupId : checkGroupIds) {
            Map<String,Integer> map= new HashMap<>();
            map.put("setMealId",setMealId);
            map.put("checkGroupId",checkGroupId);
            setMealDao.setSetmealAndCheckGroup(map);
        }
    }
}
