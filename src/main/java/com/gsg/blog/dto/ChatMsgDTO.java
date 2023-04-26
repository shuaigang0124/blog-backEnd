package com.gsg.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2022/9/6 17:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ChatMsgDTO extends Page implements Serializable {
    private static final long serialVersionUID = -6455492020284634783L;


    /**
     * 聊天消息表主键
     */
    private String id;

    /**
     * 发送者
     */
    @NotNull(message = "userId is not null")
    private String userId;
    /**
     * 群聊id
     */
    @NotNull(message = "roomId is not null")
    private String roomId;

    /**
     * 聊天消息
     */
    @NotNull(message = "content is not null")
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
    @NotNull(message = "sendTime is not null")
    private String sendTime;

}
