package com.example.springbootmybatis.service;

import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;

import javax.servlet.http.HttpServletRequest;

public interface ILoginService {
    JsonRespBo login(User user, HttpServletRequest request) throws Exception;

    boolean logOut(HttpServletRequest request);
}
