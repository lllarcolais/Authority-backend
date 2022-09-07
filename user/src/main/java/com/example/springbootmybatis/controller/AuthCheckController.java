package com.example.springbootmybatis.controller;

import com.example.springbootmybatis.annotation.Login;
import com.example.springbootmybatis.pojo.AuthMenu;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthCheckController {

    @Autowired
    private AuthService authService;

//    @GetMapping("/{userid}")
//    public JsonRespBo listMenu(@PathVariable("userid") Integer userId){
//
//    }

    @GetMapping("")
    public List<AuthMenu> listAuthMenu(){
        return authService.listAuthMenu();
    }

    @GetMapping("/raw/{userid}")
    public List<AuthMenu> rawAuthMenu(@PathVariable("userid") Integer userId){
        return authService.rawUserMenu(userId);
    }

    @GetMapping("/{userid}")
    @CrossOrigin
    public JsonRespBo listAuthMenu(@PathVariable("userid") Integer userId){
        return authService.userMenu(userId);
    }

    @GetMapping("/url/{userid}")
    @CrossOrigin
    @Login
    public List authUrl(@PathVariable("userid") Integer userId){
        return authService.authUrl(userId);
    }
}
