package com.chat.mapper;

import com.chat.model.entity.Msg;
import org.apache.ibatis.annotations.Insert;

public interface MsgVOMapper {

    @Insert("INSERT INTO msg m_senderid=#{mSenderId}, m_receiverid=#{mReceiverId},m_content=#{mContent},m_type=#{mType}")
    int insertMsgVO(Msg msg);
}
