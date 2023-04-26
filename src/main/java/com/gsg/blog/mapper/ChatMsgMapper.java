package com.gsg.blog.mapper;

import com.gsg.blog.dto.ChatMsgDTO;
import com.gsg.blog.model.ChatMsg;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsg.blog.vo.ChatListVO;

import java.util.List;

/**
 * <p>
 * 聊天消息表 Mapper 接口
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-24
 */
public interface ChatMsgMapper extends BaseMapper<ChatMsg> {

    ChatMsg selectLastMsg();

    List<ChatListVO> getChatList(ChatMsgDTO chatMsgDTO);

}
