package com.gsg.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/5/9 14:37
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleCommentVo implements Serializable {

    private static final long serialVersionUID = 5353523119190183380L;

    /**
     * 自定义主键
     */
    private String id;

    /**
     * 日常分享表主键ID
     */
    private String articleId;

    /**
     * 用户表主键
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 被评论人主键
     */
    private String beCommentedUserId;

    /**
     * 被回复的用户名
     */
    private String userNameByReply;

    /**
     * 评论父级id
     */
    private String parentId;

    /**
     * 评论等级
     */
    private Integer level;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Integer clickNum;

    /**
     * 点赞状态
     */
    private boolean clickState;

    /**
     * 子评论条数
     */
    private Integer total;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtCreate;

}
