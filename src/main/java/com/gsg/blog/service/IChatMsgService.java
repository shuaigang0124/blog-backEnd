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

    /**
     * 发送聊天信息
     * @param chatMsgDTO dto
     */
    void sendMsgToUser(ChatMsgDTO chatMsgDTO);

    /**
     * 获取mq聊天信息
     * @param queueName 队列名
     * @return  list
     */
    List<Object> getMsg(String queueName);

    /**
     * 保存聊天信息
     * @param chatMsg 聊天信息
     */
    void saveMsg(ChatMsg chatMsg);

    /**
     * 从数据库获取聊天信息
     * @param chatMsgDTO 聊天信息
     * @return  list
     */
    List<ChatListVO> getChatList(ChatMsgDTO chatMsgDTO);

    /**
     * 获取用户聊天列表
     * @param chatMsgDTO chatMsgDTO
     * @return list
     */
    List<ChatListVO> getChatRoomList(ChatMsgDTO chatMsgDTO);

}
