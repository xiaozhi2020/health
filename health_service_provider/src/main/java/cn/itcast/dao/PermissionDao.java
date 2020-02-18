package cn.itcast.dao;

import cn.itcast.pojo.Permission;

import java.util.Set;

public interface PermissionDao {
    //根据角色id查询权限
    Set<Permission> findByRid(Integer roleId);
}
