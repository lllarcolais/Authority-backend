package com.example.springbootmybatis.service;

import com.example.springbootmybatis.constant.Constants;
import com.example.springbootmybatis.dao.DataPermissionDao;
import com.example.springbootmybatis.dao.RoleDao;
import com.example.springbootmybatis.pojo.Role;
import com.example.springbootmybatis.util.UserInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DataPermissionImpl implements DataPermission{

    private static final Logger logger = LoggerFactory.getLogger(DataPermissionImpl.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private DataPermissionDao dataPermissionDao;

    @Override
    public Map getDataPermissionCondition(String resource) {
        List<String> userRole = UserInfoUtils.getRole();
        Integer userId = UserInfoUtils.getUserId();
        Map<String, Object> resultMap = new HashMap<>();

        if (userRole != null) {
            Set<String> userPermissionList = new HashSet<>();
            Set<String> dataPermission = new HashSet<>();

            for (String roleName: userRole) {
//                这里从roleDao query了，是否有更好做法？
                Role role = roleDao.queryRoleByName(roleName);
                String permission = role.getDataPermission();
                userPermissionList.addAll(Arrays.asList(permission.split("\\|")));
            }

            if (userPermissionList.contains(Constants.DATA_PERMISSION_ALL)) {
                resultMap.put("allData", Constants.DATA_PERMISSION_ALL);
            }

            for (String s: userPermissionList) {
                switch (s) {
                    case Constants.DATA_PERMISSION_CREATE_BY_MYSELF:
                        dataPermission.addAll(getCreateByMyselfData(userId, resource));
                        break;
                    case Constants.DATA_PERMISSION_AUTHORIZED:
                        dataPermission.addAll(getAuthorizedData(userId, resource));
                    default:
                        break;
                }
            }
            resultMap.put("dataPermission", dataPermission);
        } else {
            logger.warn("无法获取用户角色！");
        }
        return resultMap;
    }

    @Override
    public List getCreateByMyselfData(Integer userId, String resource) {
        return dataPermissionDao.getCreateByMyselfData(userId, resource);
    }

    @Override
    public List getAuthorizedData(Integer userId, String resource) {
        return dataPermissionDao.getAuthorizedData(userId, resource, null);
    }
}
