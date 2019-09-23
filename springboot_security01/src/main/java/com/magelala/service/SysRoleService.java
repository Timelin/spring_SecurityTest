package com.magelala.service;

import com.magelala.dao.SysRoleMapper;
import com.magelala.entity.SysRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName:SysRoleService
 * @Author:Timelin
 **/
@Service
public class SysRoleService {

    @Autowired
    private SysRoleMapper roleMapper;

    public SysRole selectById(Integer id){
        return roleMapper.selectById(id);
    }


    public SysRole selectByName(String roleName) {
        return roleMapper.selectByName(roleName);
    }
}
