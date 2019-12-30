package com.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.model.entity.Friend;
import org.apache.ibatis.annotations.Select;

public interface FriendMapper extends BaseMapper<Friend> {

    @Select("SELECT * FROM friend WHERE f_curuser=#{fCuruser} and f_buser=#{fBuser}")
    Friend findFriend(Friend friend);

}
