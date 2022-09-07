package com.example.springbootmybatis.dao;

import com.example.springbootmybatis.pojo.AuthMenu;
import com.example.springbootmybatis.pojo.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AuthDao {
    public List<AuthMenu> listAuthMenu();

    public List<AuthMenu> rawUserMenu(Integer userId);

    public List<AuthMenu> authUrl(Integer userId);

    public List<Role> getRole(Integer userId);
}
