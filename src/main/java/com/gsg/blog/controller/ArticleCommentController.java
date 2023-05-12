package com.gsg.blog.controller;

import com.gsg.blog.dto.CommonDto;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.model.ArticleComment;
import com.gsg.blog.service.IArticleCommentService;
import com.gsg.blog.utils.PkGenerator;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
import com.gsg.blog.vo.ArticleCommentVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 博客-评论表 前端控制器
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
@RestController
@RequestMapping("/gsg/articleComment")
public class ArticleCommentController extends BaseController {

    @Autowired
    private IArticleCommentService articleCommentService;

    @PostMapping("/insert")
    public Result<?> insert(@Valid @RequestBody ArticleComment articleComment) {
        articleComment.setId("CMT" + PkGenerator.generate());
        if (StringUtils.isNotEmpty(articleComment.getBeCommentedUserId())) {
            articleComment.setLevel(1);
        }
        articleCommentService.save(articleComment);
        return result(R.ok());
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody ArticleComment articleComment) {
        if (StringUtils.isEmpty(articleComment.getArticleId())) {
            throw ServiceException.errorParams();
        }
        articleCommentService.updateById(articleComment);
        return result(R.ok());
    }

    @PostMapping("/getList")
    public Result<?> getList(@RequestBody ArticleComment articleComment) {
        Integer num = articleComment.getPageNum();
        Integer size = articleComment.getPageSize();
        if (num == null || size == null || articleComment.getLevel() == null
                || StringUtils.isEmpty(articleComment.getArticleId())) {
            throw ServiceException.errorParams();
        }
        List<ArticleCommentVo> list = articleCommentService.getListArticleComment(articleComment);
        return result(R.ok(pageVo(num, size, list.size(),
                articleCommentService.getListArticleCommentTotal(articleComment), list)));
    }

    @PostMapping("/delete")
    public Result<?> delete(@RequestBody ArticleComment articleComment) {
        if (StringUtils.isEmpty(articleComment.getId())) {
            throw ServiceException.errorParams();
        }

        articleComment.setDeleted(1);
        boolean row = articleCommentService.updateById(articleComment);
        if (!row) {
            throw ServiceException.busy();
        }
        return result(R.ok());
    }

    @PostMapping("/kudos")
    public Result<?> kudos(@RequestBody CommonDto commonDto) {
        articleCommentService.kudos(commonDto);
        return result(R.ok());
    }
}
