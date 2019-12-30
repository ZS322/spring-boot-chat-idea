package com.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chat.model.entity.Msg;
import com.chat.model.vo.MsgVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface MsgMapper extends BaseMapper<Msg> {

    //查询登录和某个用户的全部聊天信息
    @Select("SELECT m_content,m_type,m_createtime FROM msg WHERE m_senderid=#{senderId} and m_receiverid=#{userId}")
    List<Msg> contentMsg(@Param("senderId")Integer senderId,@Param("userId")Integer userId);




}
