package cn.itcast.service.impl;

import cn.itcast.dao.PermissionDao;
import cn.itcast.dao.RoleDao;
import cn.itcast.dao.UserDao;
import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.pojo.User;
import cn.itcast.service.UserService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    /**
     * 查询用户
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        //根据用户名查询用户
        User user = userDao.findByUsername(username);
        if (user==null){
            return null;
        }
        Integer userId = user.getId();
        //根据用户id查询角色
        Set<Role> roles = roleDao.findByUid(userId);
        //遍历角色
        for (Role role : roles) {
            Integer roleId = role.getId();
            //根据角色id查询权限
            Set<Permission> permissions = permissionDao.findByRid(roleId);
            //权限添加到角色
            role.setPermissions(permissions);
        }
        //角色添加到用户
        user.setRoles(roles);
        return user;
    }
}
