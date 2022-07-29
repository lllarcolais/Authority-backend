package com.example.springbootmybatis.service;

import com.example.springbootmybatis.dao.UserDao;
import com.example.springbootmybatis.pojo.User;
import com.example.springbootmybatis.pojo.query.UserQuery;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // 交由spring容器管理
public class UserServiceImpl implements UserService{

    @Autowired
    private UserDao userDao;

    @Override
    public List<User> listUser() {
        return userDao.listUser();
    }

    @Override
    public User queryUserByName(String name) {
        return userDao.queryUserByName(name);
    }

    @Override
    public PageInfo<User> listUserByName(UserQuery userQuery) {
        PageHelper.startPage(userQuery.getPageNum(),userQuery.getPagesize());
        return new PageInfo<User>(userDao.listUserByName(userQuery));
    }

    @Override
    public boolean deleteUserById(Integer id) {
        int i = userDao.deleteUserById(id);
        if (i>0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public User queryUserById(Integer id) {
        return userDao.queryUserById(id);
    }

    @Override
    public boolean updateUser(User user) {
        int i = userDao.updateUser(user);
        if (i>0){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean addUser(User user) {
        int i = userDao.addUser(user);
        if (i>0){
            return true;
        } else {
            return false;
        }
        // return userDao.addUser(user)>0 ? true : false;
    }

}
