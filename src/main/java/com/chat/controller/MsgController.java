package com.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.mapper.MsgMapper;
import com.chat.model.entity.Msg;
import com.chat.model.entity.User;
import com.chat.utils.MsgUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Resource
    private MsgMapper msgMapper;

    //查询当前登录用户的与自己好友的全部历史消息
    @GetMapping("/findList")
    public ArrayList<Object> findList(HttpSession uSession) {

        User user = (User) uSession.getAttribute("user");   //获取当前登录的用户

        Msg msg = new Msg();

        msg.setmReceiverId(user.getUserId());   //接收者Id 就是登录的用户ID

        QueryWrapper qw = new QueryWrapper(msg);    //构建将要查询的Msg对象

        List<Msg> msgList = msgMapper.selectList(qw);   //  selectList查询全部的消息

        Integer tempId = 0;
        ArrayList<Object> result = new ArrayList<>();
        List<Msg> list = new ArrayList<>();
        for (Msg ms : msgList) {       //遍历查询到的全部消息

            Integer getSenderId = ms.getmSenderId();

            if (tempId.equals(getSenderId)) {       //如果和上次缓存的senderid相等
                list.add(ms);       //追加到相同的数组
            } else {                //如果不相等
                if (tempId != 0) {

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("senderId", tempId);
                    hashMap.put("msgList", list.toArray());
                    result.add(hashMap);
                }
                list.clear();
                list.add(ms);
            }
            tempId = getSenderId;
        }

        HashMap<String, Object> hp = new HashMap<>();
        hp.put("senderId", tempId);
        hp.put("msgList", list.toArray());
        result.add(hp);
        return result;
    }

    @GetMapping("/contentMsg/{senderId}")
    public MsgUtil contentMsg(@PathVariable("senderId") Integer senderId, HttpSession uSession) {

        User user = (User) uSession.getAttribute("user");   //获取当前登录的用户

        List<Msg> contentMsg = msgMapper.contentMsg(senderId, user.getUserId()); //执行查询

        return new MsgUtil(200, "查询成功", contentMsg);  //返回成功的消息

    }


    @PostMapping("/updateByIdentity")
    public MsgUtil updateByIdentity(@RequestBody Map<String, Object> map, HttpSession uSession) {

        Object sender = map.get("m_senderid");

        User user = (User) uSession.getAttribute("user");   //获取当前登录的用户

        int rows = msgMapper.updateByIdentity(Integer.parseInt((String) sender), user.getUserId());

        System.out.println(rows);

        return (rows > 0) ? new MsgUtil(200, "修改成功", null) : new MsgUtil(400, "修改失败", null);

    }

//
//    //    //无需返回值
//    public int insertTo(MsgVO msgVO) {  //接收msg  对象参数
//
//        Msg msg = new Msg(msgVO.getSenderId(), msgVO.getReceiverId(), msgVO.getmContent(), msgVO.getmType());
//        msg.setmIdentity(1);
////        int insert = msgMapper.insert(msg);//真正执行sql 插入记录
//
////        System.out.println(insert);
//
////        return insert;
//        return msgVOMapper.insertMsgVO(msg);
//    }
////
////    public void insertMsgVO(MsgVO msgVO){  //接收msg  对象参数
////
////        Msg msg=new Msg(msgVO.getSenderId(),msgVO.getReceiverId(),msgVO.getmContent(),msgVO.getmType());
////        int insert = msgMapper.insert(msg);
////        System.out.println(insert);
////
////    }

}
