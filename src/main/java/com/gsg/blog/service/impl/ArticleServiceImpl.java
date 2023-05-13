package com.gsg.blog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.gsg.blog.dto.CommonDto;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.model.Article;
import com.gsg.blog.mapper.ArticleMapper;
import com.gsg.blog.model.Tag;
import com.gsg.blog.model.User;
import com.gsg.blog.service.IArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.utils.PkGenerator;
import com.gsg.blog.vo.ArticleVo;
import com.gsg.blog.vo.TagVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 博客表 服务实现类
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public List<ArticleVo> getListArticle(Article article) {
        return articleMapper.getListArticle(article);
    }

    @Override
    public Integer getListArticleTotal(Article article) {
        return articleMapper.getListArticleTotal(article);
    }

    @Override
    public ArticleVo getArticleById(Article article) {
        if (StringUtils.isEmpty(article.getId())) {
            throw ServiceException.errorParams();
        }
        Article article1 = articleMapper.selectById(article.getId());
        ArticleVo articleVo = new ArticleVo();
        BeanUtils.copyProperties(article1, articleVo);
        if (StringUtils.isEmpty(articleVo.getUserId())) {
            throw ServiceException.error("作者信息获取异常!");
        }
        User user = userMapper.selectById(articleVo.getUserId());
        BeanUtils.copyProperties(user, articleVo);

        List<TagVo> tags = articleMapper.getTags(article.getId());
        articleVo.setTags(tags);

        return articleVo;
    }

    @Override
    public List<ArticleVo> getArticleByTagId(Tag tag) {
        if (tag.getId() == null) {
            throw ServiceException.errorParams();
        }
        List<ArticleVo> list = articleMapper.getArticleByTagId(tag);
        for (ArticleVo articleVo : list) {
            if (StringUtils.isEmpty(articleVo.getId())) {
                throw ServiceException.error("博客信息获取异常!");
            }
            List<TagVo> tags = articleMapper.getTags(articleVo.getId());
            articleVo.setTags(tags);
        }
        return list;
    }

    @Override
    public Integer getArticleTotalByTagId(Tag tag) {
        if (tag.getId() == null) {
            throw ServiceException.errorParams();
        }
        return articleMapper.getArticleTotalByTagId(tag);
    }

}
