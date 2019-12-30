package com.chat.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chat.mapper.FriendMapper;
import com.chat.mapper.UserMapper;
import com.chat.model.dto.FriendDTO;
import com.chat.model.entity.Friend;
import com.chat.model.entity.User;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")     //设置跨域跨域请求的响应
@RestController
@RequestMapping("/friend")
public class FriendController {

    @Resource
    private FriendMapper friendMapper;

    @Resource
    private UserMapper userMapper;

    @GetMapping("/findList")        //查询当前登录用户的全部好友
    public FriendDTO findList(HttpSession uSession) {

        //当前登录的用户所有信息
        User user = (User) uSession.getAttribute("user");

        Friend friend = new Friend();

        //   user.getUserId() 获取用户的id
        if (user.getUserId() != null) {
            friend.setfCuruser(user.getUserId());
        }

        QueryWrapper findFriendList = new QueryWrapper(friend);

        List getFriendList = friendMapper.selectList(findFriendList);   //查询当前登录用户的所有好友

        List<User> userList = new ArrayList<>();    //编写一个用户集合

        for (int i = 0; i < getFriendList.size(); i++) {   //遍历查询到的全部好友

            Friend getFriend = (Friend) getFriendList.get(i);

            Integer userId = getFriend.getfBuser();     //获取好友的ID

            User getUser = new User(userId);    //将获取好友的ID 存入User对象

            QueryWrapper qw = new QueryWrapper(getUser);      //构建查询对象

            User returnUser = userMapper.selectOne(qw);       //  selectOne执行查询单个实体对象

            returnUser.setUserCreateTime(null);     //把查询到的User对象中的时间设置为null

            returnUser.setUserPwd(null);            //把查询到的User对象中的密码设置为null

            userList.add(returnUser);   //将查询到的User对象存入集合

        }

        return new FriendDTO(200, "查询成功", userList);  //返回User对象集合

    }

}
