package com.magelala.service;

import com.magelala.dao.SysUserRoleMapper;
import com.magelala.entity.SysUser;
import com.magelala.entity.SysUserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:SysUserRoleService
 * @Author:Timelin
 **/

@Service
public class SysUserRoleService {

    @Autowired
    private SysUserRoleMapper userRoleMapper;

    public List<SysUserRole> listByUserId(Integer userId){
        return userRoleMapper.listByUserId(userId);
    }

}
