package com.magelala.dao;

import com.magelala.entity.SysPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SysPermissionMapper {

    @Select("select * from sys_permission where role_id=#{roleId}")
    List<SysPermission> listByRoleId(Integer roleId);

}
