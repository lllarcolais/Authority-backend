package com.example.springbootmybatis.dao;

import com.example.springbootmybatis.annotation.DataPermission;
import com.example.springbootmybatis.pojo.Data_Permission;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataPermissionDao {
    @DataPermission(enable = false)
    List<Data_Permission> queryDataPermission(Integer userId, Integer resourceId);

    @DataPermission(enable = false)
    List getCreateByMyselfData(Integer userId, String resource);

    @DataPermission(enable = false)
    List getAuthorizedData(Integer userId, String resource, Integer id);
}
