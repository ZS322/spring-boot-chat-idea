package com.chat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User implements Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;      //用户的id

    private String userName;        //用户名

    private String userPwd;             //用户的密码

    @TableField(value = "user_imgurl")      //存放网络头像的地址
    private String userImgUrl;

    @TableField(value = "user_createtime")  //用户创建的时间
    private Date userCreateTime;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    public Date getUserCreateTime() {
        return userCreateTime;
    }

    public void setUserCreateTime(Timestamp userCreateTime) {
        this.userCreateTime = userCreateTime;
    }

    public User(Integer userId) {      //便于通过用户主键查询某用户
        this.userId = userId;
    }

    public User() {

    }

    public User(Integer userId, String userName, String userPwd, String userImgUrl, Timestamp userCreateTime) {
        this.userId = userId;
        this.userName = userName;
        this.userPwd = userPwd;
        this.userImgUrl = userImgUrl;
        this.userCreateTime = userCreateTime;
    }
}
