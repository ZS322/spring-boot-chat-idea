package com.chat.service;

import com.alibaba.fastjson.JSONObject;
import com.chat.model.entity.Msg;
import com.chat.model.vo.MsgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

@ServerEndpoint(value = "/ws/controller/{userId}")    //Endpoint  相当于Servelt
@Component
public class WebSocketServer {

    private static final AtomicInteger OnlineCount = new AtomicInteger(0);

    // concurrent包的线程安全Set，用来存放每个客户端对应的Session对象。
    private static CopyOnWriteArraySet<Session> SessionSet = new CopyOnWriteArraySet<Session>();

    private static Map<String, Session> map = new HashMap<>();

    private Session session1 = null;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") String userId, Session session) {

        map.put(userId, session);
        session1 = map.get(userId);
        SessionSet.add(session1);
        int cnt = OnlineCount.incrementAndGet();             // 在线数加1
        System.out.println(cnt);
        SendMessage(session1, "连接成功");
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        SessionSet.remove(session);
        int cnt = OnlineCount.decrementAndGet();
        System.out.println(cnt);
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {

        SendMessage(session, "收到消息，消息内容：" + message);
    }


    /**
     * 发送消息，实践表明，每次浏览器刷新，session会发生变化。
     *
     * @param session
     * @param message
     */
    public static void SendMessage(Session session, String message) {

        try {
            session.getBasicRemote().sendText( message);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    /**
     * 指定Session发送消息
     *
     * @throws IOException
     */

    public static boolean SendMessage(MsgVO msgVO) throws IOException {

        Session session = null;

        for (Session session2 : SessionSet) {

            for (Map.Entry<String, Session> sessionEntry : map.entrySet()) {

                if (msgVO.getReceiverId().toString().equals(sessionEntry.getKey())) {

                    session = session2;

                    break;
                }

            }

        }

        if (session != null) {     //用户存在/在线

            JSONObject jsonObject = new JSONObject();   //构建json类

            String data = jsonObject.toJSONString(msgVO);     //把对象转换为json格式对象

            SendMessage(session, data); //发送

            return true;

        } else {

            return false;

        }

    }
}