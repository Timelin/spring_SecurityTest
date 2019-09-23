package com.magelala.service;

import com.magelala.dao.SysUserMapper;
import com.magelala.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName:SysUserService
 * @Author:Timelin
 **/
@Service
public class SysUserService {
    @Autowired
    private SysUserMapper userMapper;

    public  SysUser selectById(Integer id){
        return userMapper.selectById(id);
    }

    public  SysUser selectByName(String  name){
        return userMapper.selectByName(name);
    }

}
