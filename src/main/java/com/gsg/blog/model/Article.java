package com.gsg.blog.model;

import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gsg.blog.dto.CommonDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 博客表
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("article")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article extends CommonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自定义主键
     */
    @TableId("id")
    private String id;

    /**
     * 用户表主键
     */
    @NotNull(message = "userId is not null")
    @TableField("user_id")
    private String userId;

    /**
     * 标题
     */
    @NotNull(message = "title is not null")
    @TableField("title")
    private String title;

    /**
     * 描述
     */
    @NotNull(message = "description is not null")
    @TableField("description")
    private String description;

    /**
     * 正文
     */
    @NotNull(message = "content is not null")
    @TableField("content")
    private String content;

    /**
     * 预览图
     */
    @TableField("img")
    private String img;

    /**
     * 点赞数,默认0
     */
    @TableField("click_num")
    private Integer clickNum;

    /**
     * 浏览量
     */
    @TableField("read_num")
    private Integer readNum;

    /**
     * 是否原创；0-是1-否
     */
    @TableField("is_originality")
    private Integer isOriginality;

    /**
     * 原作者
     */
    @TableField("old_user_name")
    private String oldUserName;

    /**
     * 转载发表时间
     */
    @TableField("old_time")
    private String oldTime;

    /**
     * 版权声明
     */
    @TableField("statement")
    private String statement;

    /**
     * 转载链接
     */
    @TableField("old_link")
    private String oldLink;

    /**
     * 是否为公告（0-否1-是默认0）
     */
    @TableField("is_notice")
    private Integer isNotice;

    /**
     * 逻辑删除,0-未删除,1-已删除,默认值0
     */
    @TableField("deleted")
    private Integer deleted;

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
