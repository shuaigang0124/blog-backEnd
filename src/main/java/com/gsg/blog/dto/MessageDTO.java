package com.gsg.blog.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2022/10/19 15:15
 */
@Data
@Accessors(chain = true)
public class MessageDTO implements Serializable {

    private static final long serialVersionUID = 7025597790764636643L;

    /**
     * 自定义主键
     */
    private String id;

    /**
     * 用户表主键
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 文字颜色
     */
    private String color;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 逻辑删除,0-未删除,1-已删除,默认值0
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 序列化
    private LocalDateTime gmtCreate;

    /**
     * 修改时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class) // 反序列化
    @JsonSerialize(using = LocalDateTimeSerializer.class) // 序列化
    private LocalDateTime gmtModified;

}
