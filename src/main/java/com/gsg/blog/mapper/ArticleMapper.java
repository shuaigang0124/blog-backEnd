package com.gsg.blog.mapper;

import com.gsg.blog.model.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsg.blog.model.Tag;
import com.gsg.blog.vo.ArticleVo;
import com.gsg.blog.vo.TagVo;

import java.util.List;

/**
 * <p>
 * 博客表 Mapper 接口
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
public interface ArticleMapper extends BaseMapper<Article> {

    List<ArticleVo> getListArticle(Article article);

    Integer getListArticleTotal(Article article);

    List<TagVo> getTags(String id);

    List<ArticleVo> getArticleByTagId(Tag tag);

    Integer getArticleTotalByTagId(Tag tag);
}
