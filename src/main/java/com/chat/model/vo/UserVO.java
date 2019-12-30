package com.chat.model.vo;

import com.chat.model.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
public class UserVO {

    private Integer state;
    private String msg;
    private boolean type;
    private User user;
}
