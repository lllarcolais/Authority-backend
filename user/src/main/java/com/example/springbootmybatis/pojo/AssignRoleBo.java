package com.example.springbootmybatis.pojo;

public class AssignRoleBo {
    private Integer userId;
    private Object allRoles;
    private Object currentRoles;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Object getAllRoles() {
        return allRoles;
    }

    public void setAllRoles(Object allRoles) {
        this.allRoles = allRoles;
    }

    public Object getCurrentRoles() {
        return currentRoles;
    }

    public void setCurrentRoles(Object currentRoles) {
        this.currentRoles = currentRoles;
    }
}
