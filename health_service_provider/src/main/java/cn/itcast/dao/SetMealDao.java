package cn.itcast.dao;

import cn.itcast.pojo.Setmeal;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface SetMealDao {



    //1.1新增套餐
    void add(Setmeal setmeal);
    //1.2新增套餐关联检查组
    void setSetMealAndCheckGroup(Map<String, Integer> map);
    //2.套餐分页查询
    Page<Setmeal> findPage(String queryString);
    //3.1编辑套餐回显基本信息
    Setmeal findById(Integer id);
    //3.2编辑套餐回显已被选中的检查组
    Integer[] findCheckGroupIdsBySetMealId(Integer id);
    //4.1编辑套餐
    void edit(Setmeal setmeal);
    //4.2编辑套餐删除关联
    void deleteAssociation(Integer id);
    //5.1删除套餐前检查他的关联
    Integer findCheckGroupCountBySetMealId(Integer id);
    //5.2删除套餐
    void delete(Integer id);
    //6.获取所有套餐,手机端需求
    List<Setmeal> getSetMeal();
    //7.手机端套餐详情查询-第一张表
    Setmeal findDetailById(Integer id);
    //8.查询总个数
    Integer findAllCount();
    //9.套餐预约占比饼形图,根据套餐查询预约
    List<Map<String, Object>> findSetmealCount();
}
