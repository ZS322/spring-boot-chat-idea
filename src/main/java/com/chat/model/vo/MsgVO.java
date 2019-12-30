package com.chat.model.vo;

import lombok.ToString;

@ToString
public class MsgVO {

    private Integer senderId;
    private Integer receiverId;
    private String mContent;
    private Integer mType;

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
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

    public MsgVO() {
    }

    public MsgVO(Integer senderId, Integer receiverId, String mContent, Integer mType) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.mContent = mContent;
        this.mType = mType;
    }
}
