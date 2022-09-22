package com.example.springbootmybatis.pojo;

public class User_Role {
    private Integer id;
    private Integer userID;
    private Object roleID;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Object getRoleID() {
        return roleID;
    }

    public void setRoleID(Object roleID) {
        this.roleID = roleID;
    }
}
