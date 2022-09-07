package com.example.springbootmybatis.dao;

import com.example.springbootmybatis.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleDao {
    public List<Role> listRole();

    public Role queryRoleById(Integer id);

    public Role queryRoleByName(String name);

    public int deleteRoleById(Integer id);

    public int updateRole(Role role);

    public int addRole(Role role);
}
