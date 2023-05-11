package com.gsg.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gsg.blog.model.UserKudos;
import com.gsg.blog.mapper.UserKudosMapper;
import com.gsg.blog.model.ArticleComment;
import com.gsg.blog.mapper.ArticleCommentMapper;
import com.gsg.blog.service.IArticleCommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.vo.ArticleCommentVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 博客-评论表 服务实现类
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements IArticleCommentService {

    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private UserKudosMapper userKudosMapper;

    @Override
    public List<ArticleCommentVo> getListArticleComment(ArticleComment articleComment) {

        List<ArticleCommentVo> list = articleCommentMapper.getListArticleComment(articleComment);
        for (ArticleCommentVo vo : list) {
            ArticleComment ac = new ArticleComment();
            BeanUtils.copyProperties(articleComment, ac);
            if (ac.getLevel() == 0) {
                ac.setParentId(vo.getId())
                        .setLevel(1);
                vo.setTotal(articleCommentMapper.getListArticleCommentTotal(ac));
            }
            if (StringUtils.isNotEmpty(ac.getNowUserId())) {
                LambdaQueryWrapper<UserKudos> qw = new LambdaQueryWrapper<>();
                qw.eq(UserKudos::getUserId, ac.getNowUserId());
                qw.eq(UserKudos::getServiceId, vo.getId());
                vo.setClickState(userKudosMapper.selectCount(qw) > 0);
            } else {
                vo.setClickState(false);
            }
        }

        return list;
    }

    @Override
    public Integer getListArticleCommentTotal(ArticleComment articleComment) {
        return articleCommentMapper.getListArticleCommentTotal(articleComment);
    }
}
