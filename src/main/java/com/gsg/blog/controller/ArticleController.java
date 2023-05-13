package com.gsg.blog.controller;

import cn.hutool.core.util.ObjectUtil;
import com.gsg.blog.dto.ArticleDTO;
import com.gsg.blog.dto.CommonDto;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.model.Article;
import com.gsg.blog.model.Tag;
import com.gsg.blog.model.User;
import com.gsg.blog.service.IArticleService;
import com.gsg.blog.service.IUserService;
import com.gsg.blog.utils.*;
import com.gsg.blog.vo.ArticleVo;
import com.gsg.blog.vo.PageResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.*;

/**
 * @author shuaigang
 * @date 2022/10/19 15:09
 */
@RestController
@RequestMapping("/gsg/article")
public class ArticleController extends BaseController {

    @Autowired
    ESearchUtils eSearchUtils;

    @Autowired
    private IArticleService articleService;

    /**
     * 目标索引
     */
    String indexName = "article";

    @PostMapping("/insertArticle")
    public Result<?> insertArticle(@RequestBody ArticleDTO articleDTO) {

        if (StringUtils.isEmpty(articleDTO.getUserId())
                || articleDTO.getClassifyId() == null
                || ObjectUtil.isEmpty(articleDTO.getTagIds())
                || StringUtils.isEmpty(articleDTO.getTitle())
                || StringUtils.isEmpty(articleDTO.getContent())
        ) {
            throw ServiceException.errorParams();
        }

        if (articleDTO.getSort() == null) {
            articleDTO.setSort(0);
        }
        if (articleDTO.getOriginal() == null) {
            articleDTO.setOriginal(0);
        }

        articleDTO.setId("ATC" + PkGenerator.generate())
                .setReadNum(0)
                .setClickNum(0)
                .setDeleted(0)
                .setGmtCreate(new Date())
                .setGmtModified(new Date());
        // 存数据库

        // 存es
        eSearchUtils.doc.createOrUpdate(indexName, articleDTO.getId(), articleDTO);

        return Result.ok(BaseUtil.encode(R.ok("发布成功")));
    }

    @PostMapping("/getArticle")
    public Result<?> getArticle(@RequestBody Page page) {
        List<String[]> sortList = new ArrayList<>();
        String[] s1 = {"sort", "Desc"};
        String[] s2 = {"gmtCreate", "Desc"};
        sortList.add(s1);
        sortList.add(s2);
        PageResponseVO<Object> vo = eSearchUtils.doc.queryPage(indexName, null, null, page, sortList);
        String userId = null;
        String userName = null;
        String avatar = null;
        for (Object obj : vo.getResultList()) {
            Map<String, Object> m = (Map) obj;
            if (ObjectUtil.isNotEmpty(m.get("userId"))) {
                if (!m.get("userId").toString().equals(userId)) {
                    List<Object> user = eSearchUtils.doc.queryById("user", m.get("userId").toString());
                    Map<?, ?> u = (Map) user.get(0);
                    m.put("userName", u.get("userName"));
                    m.put("avatar", u.get("avatar"));
                    userId = m.get("userId").toString();
                    userName = u.get("userName").toString();
                    avatar = m.get("avatar").toString();
                } else {
                    m.put("userName", userName);
                    m.put("avatar", avatar);
                }
            } else {
                throw ServiceException.notFound("未获取到作者主键id");
            }
        }
        return Result.ok(BaseUtil.encode(R.ok(vo)));
    }

    @PostMapping("/insert")
    public Result<?> insert(@Valid @RequestBody Article article) {
        article.setId("ATC" + PkGenerator.generate());
        if (article.getIsOriginality() != 0 && article.getIsOriginality() != null) {
            if (StringUtils.isEmpty(article.getOldUserName())
                    || StringUtils.isEmpty(article.getOldTime())
                    || StringUtils.isEmpty(article.getOldLink())
                    || StringUtils.isEmpty(article.getStatement())) {
                throw ServiceException.errorParams();
            }
        }
        articleService.save(article);
        return result(R.ok());
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody Article article) {
        if (StringUtils.isEmpty(article.getId())) {
            throw ServiceException.errorParams();
        }
        if (article.getIsOriginality() != null && article.getIsOriginality() != 0) {
            if (StringUtils.isEmpty(article.getOldUserName())
                    || StringUtils.isEmpty(article.getOldTime())
                    || StringUtils.isEmpty(article.getOldLink())
                    || StringUtils.isEmpty(article.getStatement())) {
                throw ServiceException.errorParams();
            }
        }
        articleService.updateById(article);
        return result(R.ok());
    }

    @PostMapping("/getList")
    public Result<?> getList(@RequestBody Article article) {
//        LambdaQueryWrapper<Article> qw = new LambdaQueryWrapper<>();
//        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Article> objectPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10);
//        qw.eq(Article::getId, article.getId());
//        qw.like(StringUtils.isEmpty(article.getId()), Article::getId, article.getId());
//        qw.like(StringUtils.isEmpty(article.getId()), Article::getId, article.getId());
//        qw.orderByDesc(Article::getGmtCreate);
//        articleService.page(objectPage, qw);
        Integer pageNum = article.getPageNum();
        Integer pageSize = article.getPageSize();
        if (pageNum == null || pageSize == null) {
            throw ServiceException.errorParams();
        }
        List<ArticleVo> list = articleService.getListArticle(article);
        return result(R.ok(pageVo(pageNum, pageSize, list.size(),
                articleService.getListArticleTotal(article), list)));
    }

    @PostMapping("/getAtcByTagId")
    public Result<?> getAtcByTagId(@RequestBody Tag tag) {
        List<ArticleVo> list = articleService.getArticleByTagId(tag);
        if (tag.getPageNum() == null || tag.getPageSize() == null) {
            return result(R.ok(list));
        }
        return result(R.ok(pageVo(tag.getPageNum(),
                tag.getPageSize(),
                list.size(),
                articleService.getArticleTotalByTagId(tag),
                list)));
    }

    @PostMapping("/getById")
    public Result<?> getById(@RequestBody Article article) {
        return result(R.ok(articleService.getArticleById(article)));
    }

    @PostMapping("/delete")
    public Result<?> delete(@RequestBody CommonDto commonDto) {
        articleService.removeByIds(commonDto.getIds());
        return result(R.ok());
    }

}
