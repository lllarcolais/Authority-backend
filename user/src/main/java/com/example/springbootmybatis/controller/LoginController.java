package com.example.springbootmybatis.controller;

import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    @Autowired
    private ILoginService loginService;

    @Value("${RSA.Encrypt.PublicKey1}") String publicKey;

    @GetMapping("/pubkey")
    @CrossOrigin
    public String getPubKey(HttpServletRequest request, HttpServletResponse response){
        return publicKey;
    }


    @PostMapping("/login")
    @CrossOrigin
    public JsonRespBo loginAuth(HttpServletRequest request, HttpServletResponse response, @RequestBody User user) throws Exception {
        JsonRespBo result = loginService.login(user, request);
        return result;
    }

    @GetMapping("/logout")
    @CrossOrigin
    public boolean logOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean b = loginService.logOut(request);
        return b;
    }
}
