package com.gsg.blog.service;

import com.gsg.blog.dto.CommonDto;
import com.gsg.blog.model.ArticleComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gsg.blog.vo.ArticleCommentVo;

import java.util.List;

/**
 * <p>
 * 博客-评论表 服务类
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
public interface IArticleCommentService extends IService<ArticleComment> {

    List<ArticleCommentVo> getListArticleComment(ArticleComment articleComment);

    Integer getListArticleCommentTotal(ArticleComment articleComment);

    void kudos(CommonDto commonDto);
}
