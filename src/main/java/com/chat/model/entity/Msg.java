package com.chat.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Msg {

    @TableId(value = "m_id", type = IdType.AUTO)
    private Integer mId;

    @TableField(value = "m_senderid")
    private Integer mSenderId;

    @TableField(value = "m_receiverid")
    private Integer mReceiverId;

    @TableField(value = "m_content")
    private String mContent;

    @TableField(value = "m_type")
    private Integer mType;

    @TableField(value = "m_Identity")
    private Integer mIdentity;

    @TableField(value = "m_createtime")
    private Date mCreateTime;

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getmSenderId() {
        return mSenderId;
    }

    public void setmSenderId(Integer mSenderId) {
        this.mSenderId = mSenderId;
    }

    public Integer getmReceiverId() {
        return mReceiverId;
    }

    public void setmReceiverId(Integer mReceiverId) {
        this.mReceiverId = mReceiverId;
    }

     public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public Integer getmType() {
        return mType;
    }

    public void setmType(Integer mType) {
        this.mType = mType;
    }

    public Integer getmIdentity() {
        return mIdentity;
    }

    public void setmIdentity(Integer mIdentity) {
        this.mIdentity = mIdentity;
    }


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm")
    public Date getmCreateTime() {
        return mCreateTime;
    }

    public void setmCreateTime(Date mCreateTime) {
        this.mCreateTime = mCreateTime;
    }

    public Msg() {
    }

    public Msg(Integer mSenderId, Integer mReceiverId, String mContent, Integer mType) {
        this.mSenderId = mSenderId;
        this.mReceiverId = mReceiverId;
        this.mContent = mContent;
        this.mType = mType;
    }

    public Msg(Integer mId, Integer mSenderId, Integer mReceiverId, String mContent, Integer mType, Integer mIdentity, Date mCreateTime) {
        this.mId = mId;
        this.mSenderId = mSenderId;
        this.mReceiverId = mReceiverId;
        this.mContent = mContent;
        this.mType = mType;
        this.mIdentity = mIdentity;
        this.mCreateTime = mCreateTime;
    }
}
