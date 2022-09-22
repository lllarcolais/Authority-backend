package com.example.springbootmybatis.service;

import com.example.springbootmybatis.pojo.AssignRoleBo;
import com.example.springbootmybatis.pojo.JsonRespBo;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.pojo.query.UserQuery;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface UserService {
    // 查询所有用户
    public List<User> listUser();

    //根据用户名精确查询用户
    public User queryUserByName(String name);

    // 根据用户名来查询用户 并分页展示
    public PageInfo<User> listUserByName(UserQuery userQuery);

    //根据id删除用户
    public boolean deleteUserById(Integer id);

    //根据id查询用户
    public JsonRespBo queryUserById(Integer id);

    public JsonRespBo queryRoleByUserId(Integer id);

    //修改用户
    public boolean updateUser(User user);

    //新增用户
    public boolean addUser(User user);

    public JsonRespBo assignRole(AssignRoleBo assignRoleBo);
}
