package com.example.springbootmybatis.service;

import com.example.springbootmybatis.pojo.Role;

import java.util.List;

public interface RoleService {
    public List<Role> listRole();

    public Role queryRoleById(Integer id);

    public boolean deleteRoleById(Integer id);

    public boolean updateRole(Role role);

    public boolean addRole(Role role);
}
