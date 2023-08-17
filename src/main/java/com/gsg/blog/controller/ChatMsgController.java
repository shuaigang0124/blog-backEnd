package com.gsg.blog.controller;


import com.gsg.blog.dto.ChatMsgDTO;
import com.gsg.blog.service.IChatMsgService;
import com.gsg.blog.utils.BaseUtil;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
import com.gsg.blog.vo.ChatListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 聊天消息表 前端控制器
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-24
 */
@RestController
@RequestMapping("/gsg/chat")
public class ChatMsgController {


    @Autowired
    private IChatMsgService chatMsgService;

    /**
     * 用户间聊天
     */
    @PostMapping("/sendMsg")
    public Result<?> sendMsg(@Valid @RequestBody ChatMsgDTO chatMsgDTO) {
        chatMsgService.sendMsgToUser(chatMsgDTO);
        return Result.ok(BaseUtil.encode(R.ok()));
    }

    /**
     * 获取消息
     */
    @PostMapping("/getMsg")
    public Result<?> getMsg(@RequestBody ChatMsgDTO chatMsgDTO) {
        return Result.ok(BaseUtil.encode(R.ok(chatMsgService.getMsg(chatMsgDTO.getUserId()))));
    }

    /**
     * 查询用户聊天历史数据
     */
    @PostMapping("/getChatList")
    public Result<?> getChatList(@RequestBody ChatMsgDTO chatMsgDTO) {
        List<ChatListVO> chatList = chatMsgService.getChatList(chatMsgDTO);
        return Result.ok(BaseUtil.encode(R.ok(chatList)));
    }

    /**
     * 查询用户聊天列表
     */
    @PostMapping("/getChatRoomList")
    public Result<?> getChatRoomList(@RequestBody ChatMsgDTO chatMsgDTO) {
        List<ChatListVO> chatList = chatMsgService.getChatRoomList(chatMsgDTO);
        return Result.ok(BaseUtil.encode(R.ok(chatList)));
    }

}
