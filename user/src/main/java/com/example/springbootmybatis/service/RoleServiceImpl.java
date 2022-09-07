package com.example.springbootmybatis.service;

import com.example.springbootmybatis.dao.RoleDao;
import com.example.springbootmybatis.pojo.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService{
    @Autowired
    private RoleDao roleDao;

    @Override
    public List<Role> listRole() {
        return roleDao.listRole();
    }

    @Override
    public Role queryRoleById(Integer id) {
        return roleDao.queryRoleById(id);
    }

    @Override
    public boolean deleteRoleById(Integer id) {
        int i = roleDao.deleteRoleById(id);
        if (i>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean updateRole(Role role) {
        int i = roleDao.updateRole(role);
        if (i>0){
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean addRole(Role role) {
        int i = roleDao.addRole(role);
        if (i>0){
            return true;
        }else{
            return false;
        }
    }
}
