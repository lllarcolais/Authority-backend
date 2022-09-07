package com.example.springbootmybatis.service;

import com.example.springbootmybatis.pojo.AuthMenu;
import com.example.springbootmybatis.pojo.JsonRespBo;

import java.util.List;

public interface AuthService {
    public List<AuthMenu> listAuthMenu();

    public List<AuthMenu> rawUserMenu(Integer userId);

    public JsonRespBo userMenu(Integer userId);

    public List authUrl(Integer userId);

    public List<String> getRole(Integer userId);
}
