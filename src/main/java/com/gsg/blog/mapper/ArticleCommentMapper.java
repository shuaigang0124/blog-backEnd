package com.gsg.blog.mapper;

import com.gsg.blog.model.ArticleComment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsg.blog.vo.ArticleCommentVo;

import java.util.List;

/**
 * <p>
 * 博客-评论表 Mapper 接口
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
public interface ArticleCommentMapper extends BaseMapper<ArticleComment> {

    List<ArticleCommentVo> getListArticleComment(ArticleComment articleComment);

    Integer getListArticleCommentTotal(ArticleComment articleComment);

}
