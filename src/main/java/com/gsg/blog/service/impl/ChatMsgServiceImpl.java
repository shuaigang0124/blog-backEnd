package com.gsg.blog.service.impl;

import com.gsg.blog.dto.ChatMsgDTO;
import com.gsg.blog.model.ChatMsg;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.mapper.ChatMsgMapper;
import com.gsg.blog.model.User;
import com.gsg.blog.service.IChatMsgService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.service.IUserService;
import com.gsg.blog.utils.ChatUtils;
import com.gsg.blog.utils.PkGenerator;
import com.gsg.blog.vo.ChatListVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 聊天消息表 服务实现类
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-24
 */
@Slf4j
@Service
public class ChatMsgServiceImpl extends ServiceImpl<ChatMsgMapper, ChatMsg> implements IChatMsgService {

    @Autowired
    private ChatUtils chatUtils;

    @Autowired
    private ChatMsgMapper chatMsgMapper;

    @Autowired
    private IUserService userService;

    @Override
    public void sendMsgToUser(ChatMsgDTO chatMsgDTO) {

        ChatMsg chatMsg = new ChatMsg();
        chatMsg.setUserId(chatMsgDTO.getUserId())
                .setRoomId(chatMsgDTO.getRoomId())
                .setContent(chatMsgDTO.getContent())
                .setSendTime(chatMsgDTO.getSendTime());
        // 存储消息
        saveMsg(chatMsg);
        ChatListVO chatListVO = new ChatListVO();
        BeanUtils.copyProperties(chatMsg, chatListVO);
        User userById = userService.findUserById(chatListVO.getUserId());
        chatListVO.setUserName(userById.getUserName())
                .setUserAvatar(userById.getAvatar());
        // mq发送消息
        chatUtils.publishMsg(chatListVO, chatMsgDTO.getChatType() == null ? 2 : chatMsgDTO.getChatType());
    }

    @Override
    public List<Object> getMsg(String queueName) {
        // mq发送消息
        return chatUtils.getMsg(queueName);
    }

    @Override
    public void saveMsg(ChatMsg chatMsg) {
        String chatMsgId = "ChatMsg_" + PkGenerator.generate();
        chatMsg.setId(chatMsgId);
        int insertRow = chatMsgMapper.insert(chatMsg);
        if (insertRow != 1) {
            // 是：受影响行数不是1，则插入用户数据失败，将抛出ServiceException
            log.error("插入新的聊天记录失败,[{}]", chatMsg.getUserId());
            throw ServiceException.busy();
        }
        log.info("存入的消息【{}】", chatMsg);
        /* 更新上一条聊天记录isLatest为0*/
        ChatMsg lastChatMsg = chatMsgMapper.selectLastMsg(chatMsg.getUserId(), chatMsg.getRoomId());
        if (!ObjectUtils.isEmpty(lastChatMsg)) {
            lastChatMsg.setIsLatest(0);
            chatMsgMapper.updateById(lastChatMsg);
        }
    }

    @Override
    public List<ChatListVO> getChatList(ChatMsgDTO chatMsgDTO) {
        String roomId = chatMsgDTO.getRoomId();
        Integer pageNum = chatMsgDTO.getPageNum();
        Integer pageSize = chatMsgDTO.getPageSize();
        if (StringUtils.isEmpty(roomId) || pageNum == null || pageSize == null) {
            throw ServiceException.errorParams();
        }
        return chatMsgMapper.getChatList(chatMsgDTO);
    }

    @Override
    public List<ChatListVO> getChatRoomList(ChatMsgDTO chatMsgDTO) {
        String roomId = chatMsgDTO.getRoomId();
        if (StringUtils.isEmpty(roomId)) {
            throw ServiceException.errorParams();
        }
        List<ChatListVO> list = chatMsgMapper.getChatRoomList(chatMsgDTO);
        List<String> userList = new ArrayList<>();
        List<ChatListVO> resultList = new ArrayList<>();
        for (ChatListVO vo : list) {
            if (vo.getRoomId().equals(roomId)) {
                if (!userList.contains(vo.getUserId())) {
                    userList.add(vo.getUserId());
                    User user = userService.getById(vo.getUserId());
                    vo.setUserName(user.getUserName())
                            .setUserAvatar(user.getAvatar());
                    resultList.add(vo);
                }
                continue;
            }
            if (!userList.contains(vo.getRoomId())) {
                userList.add(vo.getRoomId());
                if (!"chat-room".equals(vo.getRoomId())) {
                    User user = userService.getById(vo.getRoomId());
                    vo.setUserName(user.getUserName())
                            .setUserAvatar(user.getAvatar());
                }
                resultList.add(vo);
            }
        }
        return resultList;
    }

}
