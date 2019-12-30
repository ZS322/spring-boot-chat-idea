package com.chat.model.dto;

import com.chat.model.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FriendDTO {

    private Integer state;      //请求状态码
    private String msg;         //响应消息
    private List<User> data;    //存放数据

}
