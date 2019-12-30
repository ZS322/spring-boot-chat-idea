package com.chat;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = {"com.chat.mapper"})
public class ChatApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatApplication.class,args);

    }

}
