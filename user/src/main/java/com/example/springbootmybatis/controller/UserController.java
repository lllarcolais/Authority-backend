package com.example.springbootmybatis.controller;

import com.example.springbootmybatis.annotation.Login;
import com.example.springbootmybatis.pojo.AssignRoleBo;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.pojo.query.UserQuery;
import com.example.springbootmybatis.service.UserService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/quickpermission")
    @CrossOrigin
    public JsonRespBo permission(HttpServletRequest request) {
        JsonRespBo result = new JsonRespBo();
        List data = new ArrayList();
        List children = new ArrayList();
        Map<String, Object> child = new HashMap<>();
        Map<String, Object> dd = new HashMap<>();
        child.put("name","用户列表");
        children.add(child);
        dd.put("name","用户管理");
        dd.put("children",children);
        data.add(dd);
        result.setCode("0");
        result.setMessage("0");
        result.setData(data);
        return result;
    }

    @GetMapping("")
    @CrossOrigin
    @Login
    public List<User> listUser(HttpServletResponse response){
        return userService.listUser();
    }

    @ApiOperation("用户分页")
    @PostMapping("/index")
    @CrossOrigin
    @Login
    public PageInfo<User> index(HttpServletResponse response, @RequestBody UserQuery userQuery){

        PageInfo<User> userPageInfo = userService.listUserByName(userQuery);

        return userPageInfo;
    }


    @ApiOperation("根据id搜索")
    @GetMapping("/{id}")
    @CrossOrigin
    @Login
    public JsonRespBo findUserById(@PathVariable("id") Integer id){
        JsonRespBo respBo = userService.queryUserById(id);
        return respBo;

    }

    @GetMapping("/role/{id}")
    @CrossOrigin
    @Login
    public JsonRespBo findRoleByUserId(@PathVariable("id") Integer id){
        JsonRespBo respBo = userService.queryRoleByUserId(id);
        return respBo;

    }

    @ApiOperation("根据用户id删除用户")
    @DeleteMapping("/delete/{id}")
    @CrossOrigin
    @Login
    public boolean delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes, HttpServletResponse response){
        return userService.deleteUserById(id);
    }

    @ApiOperation("编辑用户")
    @PutMapping("/update")
    @CrossOrigin
    @Login
    public boolean update(@RequestBody User user, HttpServletRequest request){
        return userService.updateUser(user);
    }

    @ApiOperation("新增用户")
    @PostMapping("/add")
    @CrossOrigin
    @Login
    public boolean add(@RequestBody User user, HttpServletRequest request){
        return userService.addUser(user);
    }

    @PostMapping("/assignRole")
    @CrossOrigin
    @Login
    public JsonRespBo assignRole(@RequestBody AssignRoleBo assignRoleBo) {
        return userService.assignRole(assignRoleBo);
    }
}
