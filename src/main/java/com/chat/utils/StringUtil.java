package com.chat.utils;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)  //返回消息内容工具类
public class StringUtil {

    private Integer state;      //请求状态码
    private String msg;         //响应消息
    private Map<String,Object> data;    //存放数据

}
