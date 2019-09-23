package com.magelala.dao;

import com.magelala.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysUserMapper {

    // 根据用户id 查询用户
    @Select("select * from sys_user where id = #{id}")
    SysUser selectById(Integer id);

    // 根据用户名称查询用户
    @Select("select * from sys_user where name = #{name}")
    SysUser selectByName(String name);
}
