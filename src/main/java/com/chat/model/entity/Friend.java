package com.chat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Friend implements Serializable {

    @TableId(value = "f_id", type = IdType.AUTO)
    private Integer fId;

    @TableField(value = "f_curuser")
    private Integer fCuruser;

    @TableField(value = "f_buser")
    private Integer fBuser;


    @TableField(value = "f_createtime")
    private Date fCreateTime;

    public Integer getfId() {
        return fId;
    }

    public void setfId(Integer fId) {
        this.fId = fId;
    }

    public Integer getfCuruser() {
        return fCuruser;
    }

    public void setfCuruser(Integer fCuruser) {
        this.fCuruser = fCuruser;
    }

    public Integer getfBuser() {
        return fBuser;
    }

    public void setfBuser(Integer fBuser) {
        this.fBuser = fBuser;
    }

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    public Date getfCreateTime() {
        return fCreateTime;
    }

    public void setfCreateTime(Date fCreateTime) {
        this.fCreateTime = fCreateTime;
    }

    public Friend() {
    }

    public Friend(Integer fId, Integer fCuruser, Integer fBuser, Date fCreateTime) {
        this.fId = fId;
        this.fCuruser = fCuruser;
        this.fBuser = fBuser;
        this.fCreateTime = fCreateTime;
    }
}
