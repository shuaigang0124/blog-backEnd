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
public class ArticleDTO implements Serializable {

    private static final long serialVersionUID = -5961585571540376557L;

    /**
     * 自定义主键
     */
    private String id;

    /**
     * 作者Id
     */
    private String userId;

    /**
     * 分类表主键
     */
    private Integer classifyId;

    /**
     * 标签数组
     */
    private Integer[] tagIds;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 浏览量
     */
    private Integer readNum;

    /**
     * 点赞数
     */
    private Integer clickNum;

    /**
     * 排序,1-默认,(1-9)权重置顶,默认值1
     */
    private Integer sort;


    /**
     * 是否原创,0-是,1-否,默认值0
     */
    private Integer original;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date gmtModified;

    /**
     * 逻辑删除,0-未删除,1-已删除,默认值0
     */
    private Integer deleted;

}
