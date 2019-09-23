package com.magelala.dao;


import com.magelala.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SysRoleMapper {

    // 根据角色id 查询角色
    @Select("select * from sys_role  where id = #{id}")
    SysRole selectById(Integer id);

    @Select("select * from sys_role  where  name= #{name}")
    SysRole selectByName(String name);
}
