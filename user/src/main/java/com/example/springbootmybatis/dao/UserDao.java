package com.example.springbootmybatis.dao;

import com.example.springbootmybatis.annotation.DataPermission;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.pojo.query.UserQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Mapper // 告诉springboot这是一个mybatis的mapper类
@Repository // 将userdao交由spring容器管理
public interface UserDao {
    // 查询所有用户
    @DataPermission
    public List<User> listUser();

    //根据用户名精确查询用户
    @DataPermission(enable = false)
    public User queryUserByName(String name);

    // 根据用户名来查询用户 并分页展示
    @DataPermission(enable = false)
    public List<User> listUserByName(UserQuery userQuery);

    @DataPermission(enable = false)
    public List<User> listUserByIdSet(@Param("userQuery") UserQuery userquery, @Param("set") Set idSet);

    //根据id查询用户
    @DataPermission(enable = false)
    public User queryUserById(Integer id);

    //根据id删除用户
    @DataPermission
    public int deleteUserById(Integer id);

    //修改用户
    @DataPermission
    public int updateUser(User user);

    //新增用户
    public int addUser(User user);

}
