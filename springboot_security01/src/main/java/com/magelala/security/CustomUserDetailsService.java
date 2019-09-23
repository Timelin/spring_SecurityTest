package com.magelala.security;

import com.magelala.entity.SysRole;
import com.magelala.entity.SysUser;
import com.magelala.entity.SysUserRole;
import com.magelala.service.SysRoleService;
import com.magelala.service.SysUserRoleService;
import com.magelala.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName:CustomUserDetailsService
 * @Author:Timelin
 **/

// 自定义封装用户名和密码、权限的类
@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private SysUserService userService;

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 定义一个权限集
        Collection<GrantedAuthority> authorities  = new ArrayList<>();

        // 从数据库中取出用户信息
        SysUser user = userService.selectByName(username);
        // 判断用户是否存在
        if(user==null){
            throw  new UsernameNotFoundException("用户名不存在");
        }


        // 添加角色
        List<SysUserRole> userRoles = userRoleService.listByUserId(user.getId());
        for (SysUserRole userRole :userRoles){
            SysRole role = roleService.selectById(userRole.getRoleId());
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        // 返回UserDetails实现类
        return new User(user.getName(),user.getPassword(),authorities);
    }
}
