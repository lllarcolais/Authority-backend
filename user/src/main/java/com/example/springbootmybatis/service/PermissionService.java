package com.example.springbootmybatis.service;

import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.Permission;

import java.util.List;

public interface PermissionService {
    public List<Permission> listPermission();

    public JsonRespBo treePermission();

    public Permission queryPermissionById(Integer id);

    public boolean deletePermissionById(Permission permission);

    public boolean updatePermission(Permission permission);

    public boolean addPermission(Permission permission);
}
