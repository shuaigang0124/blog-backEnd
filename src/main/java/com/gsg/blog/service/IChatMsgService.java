package com.gsg.blog.service;

import com.gsg.blog.dto.ChatMsgDTO;
import com.gsg.blog.model.ChatMsg;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gsg.blog.vo.ChatListVO;

import java.util.List;

/**
 * <p>
 * 聊天消息表 服务类
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-24
 */
public interface IChatMsgService extends IService<ChatMsg> {

    void sendMsgToUser(ChatMsgDTO chatMsgDTO);

    List<Object> getMsg(String queueName);

    void saveMsg(ChatMsg chatMsg);


    List<ChatListVO> getChatList(ChatMsgDTO chatMsgDTO);

}
