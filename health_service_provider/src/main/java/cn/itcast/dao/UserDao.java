package cn.itcast.dao;

import cn.itcast.pojo.User;

public interface UserDao {
    //根据用户名查询权限
    User findByUsername(String username);
}
