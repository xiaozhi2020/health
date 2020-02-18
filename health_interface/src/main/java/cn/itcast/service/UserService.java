package cn.itcast.service;

import cn.itcast.pojo.User;

public interface UserService {
    User findByUsername(String username);
}
