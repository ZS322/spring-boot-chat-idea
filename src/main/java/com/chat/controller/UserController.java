package com.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.mapper.FriendMapper;
import com.chat.mapper.UserMapper;
import com.chat.model.dto.UserDTO;
import com.chat.model.entity.Friend;
import com.chat.model.entity.Msg;
import com.chat.model.entity.User;
import com.chat.model.vo.MsgVO;
import com.chat.model.vo.UserVO;
import com.chat.service.WebSocketServer;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")   //跨域请求
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private FriendMapper friendMapper;

    /**
     * 以bean的方式匹配前端JSON的key(Name值)  反射接收参数
     * @param user
     * @return
     */
    @PostMapping("/register")
    public UserDTO register(@RequestBody User user) {   //通过用户名/密码注册 /头像

        User getUser = userMapper.findUser(user.getUserName());

        if (getUser == null) {

            Integer row = userMapper.insert(user);  //把User对象的属性写入数据库

            return (row > 0) ? new UserDTO(200, "注册成功", null) : new UserDTO(400, "注册失败", null);

        } else {
            return new UserDTO(400, "该用户已经存在", null);
        }

    }

    //登录请求
    @PostMapping("/login")
    public UserDTO login(@RequestBody User user, HttpSession uSession) {

        QueryWrapper findUser = new QueryWrapper(user);

        User getUser = userMapper.selectOne(findUser);

        Map<String, Object> result = new HashMap<>();

        if (getUser != null) {

            uSession.setAttribute("user", getUser);     //把当前用户登录状态存入Session

            result.put("userId", getUser.getUserId());       //获取当前登录用户的主键存入Map集合

            result.put("userName", getUser.getUserName());   //获取当前登录用户的用户名存入Map集合

            result.put("userImgUrl", getUser.getUserImgUrl());    //获取当前登录用户的头像存入Map集合

            return new UserDTO(200, "登录成功", result);

        } else {

            return new UserDTO(400, "登录失败", null);
        }

    }

    //退出登录
    @GetMapping("/exit")
    public UserDTO exit(HttpSession session) {

        session.invalidate();
        return new UserDTO(200, "退出成功", null);

    }

    //添加好友
    @PostMapping("/addFriend/{f_buser}")
    public UserDTO addFriend(@PathVariable("f_buser") Integer f_buser, HttpSession uSession) {

        User user = (User) uSession.getAttribute("user");
        Friend friend = new Friend();
        friend.setfCuruser(user.getUserId());
        friend.setfBuser(f_buser);
        Friend getFriend = friendMapper.findFriend(friend);

        if (getFriend == null) {

            int row = friendMapper.insert(friend);
            return (row > 0) ? new UserDTO(200, "成功添加好友", null) : new UserDTO(400, "成功好友失败", null);
        } else {

            return new UserDTO(400, "添加失败", null);
        }

    }

    //在添加前先搜索
    @GetMapping("/userSearch/{b_user}")
    public UserVO userSearch(@PathVariable("b_user") String b_user, HttpSession uSession) {    //搜索用户功能

        User getUser = userMapper.findUser(b_user); //查询数据库是否存在该用户

        if (getUser != null) {

            User user1 = (User) uSession.getAttribute("user");           //获取当前登录用户

            Friend friend = new Friend();     //构成查询对象

            friend.setfCuruser(user1.getUserId());  //当前对象ID

            friend.setfBuser(getUser.getUserId());  //当前对象好友的ID

            Friend getFriend = friendMapper.findFriend(friend);     //执行sql语句查询数据

            //true  是登录用户好友 false   不是登录用户好友
            return (getFriend != null) ? new UserVO(200, "成功", true, getUser) : new UserVO(200, "失败", false, getUser);

        } else {

            return new UserVO(400, "失败", false, null);
        }

    }

    //搜索出未添加的好友进行提交id 添加该好友
    @PostMapping("/insert/{b_user}")
    public UserDTO insert(@PathVariable("b_user") Integer b_user, HttpSession uSession, Session session) {

        User user1 = (User) uSession.getAttribute("user");  //获取当前登录用户

        User getUser = userMapper.selectById(b_user);   //查询用户所有信息 以便后续使用

        Friend friend = new Friend();   //构成插入对象

        friend.setfCuruser(user1.getUserId());       //当前对象ID

        friend.setfBuser(b_user);       //当前对象好友的ID

        Friend friend2 = new Friend();

        friend2.setfCuruser(b_user);

        friend2.setfBuser(user1.getUserId());

        int insert = friendMapper.insert(friend);   //执行sql语句插入数据

        int insert2 = friendMapper.insert(friend2);   //执行sql语句插入数据

        Map<String, Object> result = new HashMap<>();   //构建map接口

        result.put("user", getUser);    //把用户数据存入map

        WebSocketServer.SendMessage(session, "请求添加好友成功");

        //返回成功/失败消息
        return (insert > 0 && insert2 > 0) ? new UserDTO(200, "添加成功", result) : new UserDTO(400, "添加失败", null);
    }




}
