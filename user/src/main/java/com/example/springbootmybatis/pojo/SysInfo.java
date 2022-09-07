package com.example.springbootmybatis.pojo;


import com.example.springbootmybatis.util.UserInfoUtils;

import java.sql.Timestamp;
import java.util.Date;

public class SysInfo {

    private Timestamp createTime;
    private Integer createBy;
    private Timestamp lastmodifiedTime;
    private Integer lastmodifiedBy;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime() {
        this.createTime = new Timestamp(new Date().getTime());
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy() {
        this.createBy = UserInfoUtils.getUserId();
    }

    public Date getLastmodifiedTime() {
        return lastmodifiedTime;
    }

    public void setLastmodifiedTime() {
        this.lastmodifiedTime = new Timestamp(new Date().getTime());
    }

    public Integer getLastmodifiedBy() {
        return lastmodifiedBy;
    }

    public void setLastmodifiedBy() {
        this.lastmodifiedBy = UserInfoUtils.getUserId();
    }
}
