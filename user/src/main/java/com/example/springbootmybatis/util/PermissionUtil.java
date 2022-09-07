package com.example.springbootmybatis.util;

import com.example.springbootmybatis.dao.PermissionDao;
import com.example.springbootmybatis.pojo.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

public class PermissionUtil {
    private static PermissionDao permDao;


    @Autowired
    private PermissionDao permissionDao;

    @PostConstruct
    public void init(){
        permDao = this.permissionDao;
    }

    private static Logger logger = LoggerFactory.getLogger(PermissionUtil.class);

    public static List<Permission> GetTree(List<Permission> permissions){
        List<Permission> result = new ArrayList();
        permissions.forEach(one->{
            if (one.getPid()==0){
                Permission parent = new Permission();
                parent.setId(one.getId());
                parent.setPid(one.getPid());
                parent.setPermission(one.getPermission());
                parent.setType(one.getType());
                parent.setUrl(one.getUrl());
                parent.setDescription(one.getDescription());
                List<Permission> subMenu = GetChildren(one.getId(),permissions);
                if (!subMenu.isEmpty()){
                    parent.setChildren(subMenu);
                }
                result.add(parent);
            }
        });
        return result;
    }

    public static List<Permission> GetChildren(Integer pid, List<Permission> permissions){
        List<Permission> cResult = new ArrayList();
        permissions.forEach(one->{
            if (one.getPid()==pid){
                Permission children = new Permission();
                children.setId(one.getId());
                children.setPid(one.getPid());
                children.setPermission(one.getPermission());
                children.setType(one.getType());
                children.setUrl(one.getUrl());
                children.setDescription(one.getDescription());
                List<Permission> subMenu = GetChildren(one.getId(),permissions);
                if (!subMenu.isEmpty()){
                    children.setChildren(subMenu);
                }
                cResult.add(children);
            }
        });
        return cResult;
    }

/*
* 这部分可以去掉
* */
    public static Boolean DeleteTree(List<Permission> children, List<Integer> idList){
        Boolean res = true;

        idList = GetChildrenId(children, idList);
        System.out.println(idList);
        if (idList != null){
            for (int i = 0; i < idList.size(); i++){
                int j = permDao.deletePermissionById(idList.get(i));
                if (j<0){
                    res = false;
                }
            }
        }

        return res;
    }

    public static List<Integer> GetChildrenId(List<Permission> children, List<Integer> idList){
        children.forEach(one->{
            if (one != null) {
                idList.add(one.getId());
                if (one.getChildren() != null) {
                    GetChildrenId(one.getChildren(),idList);
                }
            }
        });
        return idList;

    }
}
