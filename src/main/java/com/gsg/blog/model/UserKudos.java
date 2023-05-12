package com.gsg.blog.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户-点赞记录表(目前数据量较少，同时存储博客与评论的点赞记录)
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("user_kudos")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserKudos implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户表主键
     */
    @TableId("user_id")
    private String userId;

    /**
     * 发布业务主键
     */
    @TableField("service_id")
    private String serviceId;

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


}
