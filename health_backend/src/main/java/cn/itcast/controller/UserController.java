package cn.itcast.controller;

import cn.itcast.constant.MessageConstant;
import cn.itcast.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;

@RestController
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/getUsername")
    public Result getUsername(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(user.getUsername());
        if (user!=null){
            String username = user.getUsername();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
        } else {
            return new Result(false, MessageConstant.GET_USERNAME_FAIL);
        }
    }
}

