package com.chat.utils;

import com.chat.model.entity.Msg;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  //返回消息内容工具类
public class MsgUtil {

    private Integer state;      //请求状态码
    private String msg;         //响应消息
    private List<Msg> data;    //存放数据

}
