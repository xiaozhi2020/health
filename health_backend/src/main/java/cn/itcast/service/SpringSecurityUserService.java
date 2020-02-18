package cn.itcast.service;

import cn.itcast.pojo.Permission;
import cn.itcast.pojo.Role;
import cn.itcast.pojo.User;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {
    @Reference
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        if (user==null){
            return null;
        }
        //这里没用关联查询,是java代码实现的
        //动态为当前用户授权
        List<GrantedAuthority> grantedAuthorityList =new ArrayList<>();
        Set<Role> roles = user.getRoles();
        //遍历角色集合, 为用户授予权限
        for (Role role : roles) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.getKeyword()));
            Set<Permission> permissions = role.getPermissions();
            //遍历权限集合,为用户授予权限
            for (Permission permission : permissions) {
                grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }
        org.springframework.security.core.userdetails.User securityUser = new org.springframework.security.core.userdetails.User(username,user.getPassword(),grantedAuthorityList);
        return securityUser;
    }
}
