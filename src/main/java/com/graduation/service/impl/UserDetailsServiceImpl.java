package com.graduation.service.impl;

import com.graduation.mapper.UserMapper;
import com.graduation.model.Permission;
import com.graduation.model.Role;
import com.graduation.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        User user = userMapper.findUserByUsername(username);
        if (user == null){
            return null;
        }
        //根据用户id查询用户权限
        List<Permission> permissions = userMapper.findUserPermissionsById(user.getId());
        //权限转换String数组
        String[] auth = new String[permissions.size()];
        int i = 0;
        for(Permission permission : permissions){
            auth[i++] = permission.getName(); //{"/index.html","/question/create"}
        }
        //根据用户id查询用户角色
        List<Role> roles = userMapper.findUserRolesById(user.getId());
        //auth扩容保存用户的角色信息
        auth = Arrays.copyOf(auth,auth.length+roles.size());
        for (Role role : roles){
            auth[i++] = role.getName();
        }
        //构建UserDetails类型对象
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(auth)
                .accountLocked(user.getLocked()==1)     //账户是否锁定 false
                .disabled(user.getEnabled()==0)     //账户是否不可用 false
                .build();
        return userDetails;
    }
}
