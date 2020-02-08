package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import cn.itcast.pojo.Setmeal;
import cn.itcast.service.SetMealService;
import cn.itcast.utils.QiniuUtils;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/setMeal")
public class SetMealController {
    @Reference
    private SetMealService setMealService;

    /**
     * 文件上传
     * @param multipartFile
     * @return
     */
    /*@RequestParam("imgFile")通过该注释拿到文件对象,如果不想用该注释那么需要将形成名设置成imgFile,也就是与前端一直,而不是multipartFile;视频老师讲的有误*/
    @RequestMapping("/upload")
    public Result upload(@RequestParam("imgFile") MultipartFile multipartFile){
        try {//获取文件后缀
            String originalFilename = multipartFile.getOriginalFilename();
            int lastIndexOf = originalFilename.lastIndexOf(".");
            String fileType = originalFilename.substring(lastIndexOf - 1);

            //自定义文件名
            String fileName=UUID.randomUUID().toString()+fileType;
            try {
                QiniuUtils.upload2Qiniu(multipartFile.getBytes(),fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
            }
            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 新增套餐
     * @param setmeal
     * @param checkGroupIds
     * @return
     */
    @RequestMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkGroupIds){
        try {
            setMealService.add(setmeal,checkGroupIds);
        } catch (Exception e) {
            e.printStackTrace();
           return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }
}
