package com.gsg.blog.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;

}
