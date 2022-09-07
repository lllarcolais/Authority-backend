package com.example.springbootmybatis.dao;

import com.example.springbootmybatis.pojo.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionDao {
    public List<Permission> listPermission();

    public Permission queryPermissionById(Integer id);

    public int deletePermissionById(Integer id);

    public int updatePermission(Permission permission);

    public int addPermission(Permission permission);
}
