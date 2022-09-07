package com.example.springbootmybatis.controller;

import com.example.springbootmybatis.pojo.Role;
import com.example.springbootmybatis.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping("")
    @CrossOrigin
    public List<Role> listRole(){
        return roleService.listRole();
    };

    @GetMapping("/{id}")
    @CrossOrigin
    public Role queryRoleById(@PathVariable("id") Integer id){
        return roleService.queryRoleById(id);
    };

    @GetMapping("/delete/{id}")
    @CrossOrigin
    public boolean deleteRoleById(@PathVariable("id") Integer id){
        return roleService.deleteRoleById(id);
    };

    @PostMapping("/update")
    @CrossOrigin
    public boolean updateRole(@RequestBody Role role){
        return roleService.updateRole(role);
    };

    @PostMapping("/add")
    @CrossOrigin
    public boolean addRole(@RequestBody Role role){
        return roleService.addRole(role);
    };
}
