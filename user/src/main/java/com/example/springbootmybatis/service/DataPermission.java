package com.example.springbootmybatis.service;


import java.util.List;
import java.util.Map;

public interface DataPermission {
    Map getDataPermissionCondition(String resource);

    List getCreateByMyselfData(Integer userId, String resource);

    List getAuthorizedData(Integer userId, String resource);
}
