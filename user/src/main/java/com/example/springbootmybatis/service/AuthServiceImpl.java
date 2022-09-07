package com.example.springbootmybatis.service;

import com.example.springbootmybatis.dao.AuthDao;
import com.example.springbootmybatis.pojo.AuthMenu;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.Role;
import com.example.springbootmybatis.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private AuthDao authDao;



    @Override
    public List<AuthMenu> listAuthMenu() {
        return authDao.listAuthMenu();
    }

    @Override
    public List<AuthMenu> rawUserMenu(Integer userId) {
        return authDao.rawUserMenu(userId);
    }

    @Override
    public JsonRespBo userMenu(Integer userId) {
        JsonRespBo result = new JsonRespBo();
        try {
            List<AuthMenu> rawUserMenu = authDao.rawUserMenu(userId);
            List data = AuthUtil.GetMenu(rawUserMenu);
            if (Objects.nonNull(data) && data.size()>0) {
                result.setCode("1");
                result.setMessage("获取权限成功！");
                result.setData(data);
            } else {
                result.setCode("0");
                result.setMessage("获取权限失败！");
            }
        } catch (Exception e) {
            result.setCode("0");
            result.setMessage("获取权限错误！");
        }
        return result;
    }

    @Override
    public List authUrl(Integer userId) {
        List<AuthMenu> rawUserMenu = authDao.authUrl(userId);
        return AuthUtil.GetAuthUrl(rawUserMenu);
    }

    @Override
    public List<String> getRole(Integer userId) {
        List<Role> roleList= authDao.getRole(userId);
        List list = new ArrayList();
        System.out.println(roleList);
        for (Role role: roleList) {
            list.add(role.getName());
        }
        return list;
    }
}
