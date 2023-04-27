package com.gsg.blog.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 留言弹幕表
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 留言表主键
     */
    @TableId("id")
    private String id;

    /**
     * 发送者
     */
    @NotNull(message = "userId is not null")
    @TableField("user_id")
    private String userId;

    /**
     * 颜色
     */
    @NotNull(message = "userId is not null")
    @TableField("color")
    private String color;

    /**
     * 消息
     */
    @NotNull(message = "userId is not null")
    @TableField("content")
    private String content;

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
