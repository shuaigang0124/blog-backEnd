package com.gsg.blog.service;

import com.gsg.blog.dto.CommonDto;
import com.gsg.blog.model.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gsg.blog.model.Tag;
import com.gsg.blog.vo.ArticleVo;

import java.util.List;

/**
 * <p>
 * 博客表 服务类
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
public interface IArticleService extends IService<Article> {

    List<ArticleVo> getListArticle(Article article);

    Integer getListArticleTotal(Article article);

    ArticleVo getArticleById(Article article);

    List<ArticleVo> getArticleByTagId(Tag tag);

    Integer getArticleTotalByTagId(Tag tag);
}
