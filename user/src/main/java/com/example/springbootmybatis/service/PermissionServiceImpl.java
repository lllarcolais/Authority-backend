package com.example.springbootmybatis.service;

import com.example.springbootmybatis.dao.PermissionDao;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.Permission;
import com.example.springbootmybatis.util.PermissionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService{
    @Autowired
    private PermissionDao permissionDao;

    @Override
    public List<Permission> listPermission() {
        return permissionDao.listPermission();
    }

    @Override
    public JsonRespBo treePermission() {
        JsonRespBo result = new JsonRespBo();
        List<Permission> permissions = permissionDao.listPermission();
        List tree = PermissionUtil.GetTree(permissions);
        result.setCode("1");
        result.setMessage("获取权限树成功！");
        result.setData(tree);
        return result;
    }

    @Override
    public Permission queryPermissionById(Integer id) {
        return permissionDao.queryPermissionById(id);
    }

    @Override
    public boolean deletePermissionById(Permission permission) {
//        int i = permissionDao.deletePermissionById(permission.getId());
//        if (i > 0){
////            List<Permission> permissions = permissionDao.listPermission();
////            List<Permission> children = PermissionUtil.GetChildren(id,permissions);
//            List<Integer> idList = new ArrayList<>();
//            return PermissionUtil.DeleteTree(permission.getChildren(),idList);
//        } else {
//            return false;
//        }
        try {
            List<Integer> idList = new ArrayList<>();
            idList = PermissionUtil.GetChildrenId(permission.getChildren(),idList);
            idList.add(permission.getId());
            for (int i=0; i<idList.size(); i++){
                int j = permissionDao.deletePermissionById(idList.get(i));
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public boolean updatePermission(Permission permission) {
        int i = permissionDao.updatePermission(permission);
        if (i > 0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addPermission(Permission permission) {
        int i = permissionDao.addPermission(permission);
        if (i > 0){
            return true;
        } else {
            return false;
        }
    }
}
