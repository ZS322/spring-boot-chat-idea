package com.chat.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MsgDTO {

    private String mContent;    //返回的消息内容
    private Integer mType;      //内容类型
    private Integer mIdentity;  //内容是否已读
    private Date mCreateTime;   //发送的时间


}
