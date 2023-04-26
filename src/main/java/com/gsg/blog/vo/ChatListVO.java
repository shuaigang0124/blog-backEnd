package com.gsg.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2022/9/6 13:49
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ChatListVO implements Serializable {
    private static final long serialVersionUID = -129321366418548L;

    private String id;

    /**
     * 发送者
     */
    private String userId;
    /**
     * 发送者昵称头像
     */
    private String userName;
    private String userAvatar;
    private String roomId;

    /**
     * 聊天消息
     */
    private String content;

    /**
     * 消息类型 0-文本, 1-图片, 2-视频, 3-音频, 4-文件   暂未使用
     */
    private Integer type;

    /**
     * 是否为最后一条信息: 0-不是, 1-是
     */
    private Integer isLatest;

    /**
     * 消息发送时间
     */
    private String sendTime;


}
