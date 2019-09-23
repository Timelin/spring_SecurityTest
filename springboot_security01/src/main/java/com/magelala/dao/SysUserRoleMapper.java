package com.magelala.dao;

import com.magelala.entity.SysUser;
import com.magelala.entity.SysUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ClassName:SysUserRoleMapper
 * @Author:Timelin
 **/
@Mapper
public interface SysUserRoleMapper {

    // 根据userid 查询用户角色
    @Select("select * from sys_user_role  where user_id = #{userId}")
    List<SysUserRole> listByUserId(Integer userId);
}
