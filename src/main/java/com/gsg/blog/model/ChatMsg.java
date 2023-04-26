package com.gsg.blog.model;

import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 聊天消息表
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-24
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("chat_msg")
public class ChatMsg implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 聊天消息表主键
     */
    @TableId("id")
    private String id;

    /**
     * 发送者
     */
    @TableField("user_id")
    private String userId;

    /**
     * 群聊id
     */
    @TableField("room_id")
    private String roomId;

    /**
     * 聊天消息
     */
    @TableField("content")
    private String content;

    /**
     * 消息类型 0-文本, 1-图片, 2-视频
     */
    @TableField("type")
    private Integer type;

    /**
     * 是否为最后一条信息: 0-不是, 1-是
     */
    @TableField("is_latest")
    private Integer isLatest;

    /**
     * 消息发送时间
     */
    @TableField("send_time")
    private String sendTime;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @TableField("gmt_modified")
    private Date gmtModified;

    /**
     * 逻辑删除,0-未删除,1-已删除,默认值0
     */
    @TableField("deleted")
    private Integer deleted;


}
