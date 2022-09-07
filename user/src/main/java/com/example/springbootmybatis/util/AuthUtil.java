package com.example.springbootmybatis.util;

import com.example.springbootmybatis.pojo.AuthMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthUtil {

    public static List GetMenu(List<AuthMenu> authMenu){
        List result = new ArrayList();
        authMenu.forEach(one->{
            if (one.getPid()==0){
                Map<String, Object> parent = new HashMap<>();
                parent.put("name",one.getPermission());
                List subMenu = GetChildrenMenu(one.getPermissionID(),authMenu);
                if (!subMenu.isEmpty()){
                    parent.put("children",subMenu);
                }
                result.add(parent);
            }
        });
        return result;
    }

    public static List GetChildrenMenu(Integer pid, List<AuthMenu> authMenu){
        List cResult = new ArrayList();
        authMenu.forEach(one->{
            if (one.getPid()==pid){
                Map<String, Object> children = new HashMap<>();
                children.put("name",one.getPermission());
                List subMenu = GetChildrenMenu(one.getPermissionID(),authMenu);
                if (!subMenu.isEmpty()){
                    children.put("children",subMenu);
                }
                cResult.add(children);
            }
        });
        return cResult;
    }

    public static List GetAuthUrl(List<AuthMenu> authMenu){
        List uResult = new ArrayList();
        authMenu.forEach(one->{
            String url = one.getUrl();
            if (url!=null){
                for(String retval: url.split(",")){
                    uResult.add(retval);
                }
            }
        });
        System.out.println(uResult);
        return uResult;
    }
}
