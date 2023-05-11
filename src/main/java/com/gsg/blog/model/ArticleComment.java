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
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 博客-评论表
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName("article_comment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleComment extends CommonDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 自定义主键
     */
    @TableId("id")
    private String id;

    /**
     * 日常分享表主键ID
     */
    @NotNull(message = "articleId is not null")
    @TableField("article_id")
    private String articleId;

    /**
     * 用户表主键
     */
    @NotNull(message = "userId is not null")
    @TableField("user_id")
    private String userId;

    /**
     * 被评论人主键
     */
    @TableField("be_commented_user_id")
    private String beCommentedUserId;

    /**
     * 评论父级id
     */
    @TableField("parent_id")
    private String parentId;

    /**
     * 评论等级
     */
    @TableField("level")
    private Integer level;

    /**
     * 评论内容
     */
    @NotNull(message = "content is not null")
    @TableField("content")
    private String content;

    /**
     * 点赞数
     */
    @TableField("click_num")
    private Integer clickNum;

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
