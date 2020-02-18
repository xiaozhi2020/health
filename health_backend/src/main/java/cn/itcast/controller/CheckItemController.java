package cn.itcast.controller;


import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.PageResult;
import cn.itcast.entity.QueryPageBean;
import cn.itcast.entity.Result;
import cn.itcast.pojo.CheckItem;
import cn.itcast.service.CheckItemService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkItem")
public class CheckItemController {
    @Reference
    private CheckItemService checkItemService;

    /**
     * 新增检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){
        //System.out.println(checkItem);
        try {
            checkItemService.add(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 检查项分页
     * @param queryPageBean
     * @return
     */
    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            PageResult pageResult =  checkItemService.findPage(queryPageBean);
            return pageResult;
    }

    /**
     * 删除检查项
     * @param id
     * @return
     */
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")//权限校验
    @RequestMapping("/delete")
    public Result delete(Integer id){

        try {
            String str = checkItemService.delete(id);
            if (str!=null){//由于运行时异常走着不走catch,所以这样写
                return new Result(false, MessageConstant.DELETE_CHECKItem_FAIL_BECAUSE_ASSOCIATION);
            }
        } catch (Exception e) {
            e.printStackTrace();
            new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 编辑检查项回显
     * @param id
     * @return
     */
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckItem checkItem = checkItemService.findById(id);
           return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);

        } catch (Exception e) {
            e.printStackTrace();
           return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    /**
     * 编辑检查项
     * @param checkItem
     * @return
     */
    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){
        try {
            checkItemService.edit(checkItem);
        } catch (Exception e) {
            e.printStackTrace();
           return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
         return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    /**
     * 查询所有检查项,因为增加检查组需要
     * @return
     */
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckItem> checkItemList= checkItemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItemList);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }

}
