package com.example.springbootmybatis.controller;

import com.example.springbootmybatis.annotation.Login;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.Permission;
import com.example.springbootmybatis.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @GetMapping("")
    @CrossOrigin
    @Login
    public List<Permission> listPermission(){
        return permissionService.listPermission();
    };

    @GetMapping("/tree")
    @CrossOrigin
    @Login
    public JsonRespBo treePermission(){
        return permissionService.treePermission();
    };

    @GetMapping("/{id}")
    @CrossOrigin
    @Login
    public Permission queryPermissionById(@PathVariable("id") Integer id){
        return permissionService.queryPermissionById(id);
    };

    @PostMapping("/delete")
    @CrossOrigin
//    @Login
    public boolean deletePermissionById(@RequestBody Permission permission){
        return permissionService.deletePermissionById(permission);
    };

    @PostMapping("/update")
    @CrossOrigin
    @Login
    public boolean updatePermission(@RequestBody Permission permission){
        return permissionService.updatePermission(permission);
    };

    @PostMapping("/add")
    @CrossOrigin
    @Login
    public boolean addPermission(@RequestBody Permission permission){
        return permissionService.addPermission(permission);
    };
}
