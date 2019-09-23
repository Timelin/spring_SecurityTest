package com.magelala.service;

import com.magelala.dao.SysPermissionMapper;
import com.magelala.entity.SysPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:SysPermissionService
 * @Author:Timelin
 **/
@Service
public class SysPermissionService {

    @Autowired
    private SysPermissionMapper permissionMapper;

    /*获取指定角色所有权限*/
    public List<SysPermission> listByRoleId(Integer roleId){
        return permissionMapper.listByRoleId(roleId);
    }
}
