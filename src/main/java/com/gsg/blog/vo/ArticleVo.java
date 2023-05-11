package com.gsg.blog.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/5/9 11:10
 */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleVo implements Serializable {

    private static final long serialVersionUID = 1517538043583823033L;

    /**
     * 自定义主键
     */
    private String id;

    /**
     * 用户表主键
     */
    private String userId;

    /**
     * 用户表主键
     */
    private String userName;

    /**
     * 用户表主键
     */
    private String avatar;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 正文
     */
    private String content;

    /**
     * 预览图
     */
    private String img;

    /**
     * 点赞数,默认0
     */
    private Integer clickNum;

    /**
     * 浏览量
     */
    private Integer readNum;

    /**
     * 标签
     */
    private List<TagVo> tags;

    /**
     * 是否原创；0-是1-否
     */
    private Integer isOriginality;

    /**
     * 原作者
     */
    private String oldUserName;

    /**
     * 转载发表时间
     */
    private String oldTime;

    /**
     * 版权声明
     */
    private String statement;

    /**
     * 转载链接
     */
    private String oldLink;

    /**
     * 是否为公告0-否1-是默认0
     */
    private Integer isNotice;

    /**
     * 逻辑删除,0-未删除,1-已删除,默认值0
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date gmtModified;

}
