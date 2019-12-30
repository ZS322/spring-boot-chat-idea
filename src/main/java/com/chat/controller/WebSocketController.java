package com.chat.controller;

import com.chat.mapper.MsgMapper;
import com.chat.model.entity.Msg;
import com.chat.model.vo.MsgVO;
import com.chat.service.WebSocketServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ws")
public class WebSocketController {

    @Resource
    private MsgMapper msgMapper;

    private int rows = 0;

    /**
     * @param msgVO 指定会话ID发消息
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/sendOne")
    public void sendOneMessage(@RequestBody MsgVO msgVO) throws IOException {

        boolean flag = WebSocketServer.SendMessage(msgVO);

        Msg msg = new Msg(msgVO.getSenderId(), msgVO.getReceiverId(), msgVO.getmContent(), msgVO.getmType());

        if (flag) {

            System.out.println("在线");

            msg.setmIdentity(0);

            rows = msgMapper.insert(msg);

        } else {

            System.out.println("离线");

            msg.setmIdentity(1);

            rows = msgMapper.insert(msg);

        }


    }

}