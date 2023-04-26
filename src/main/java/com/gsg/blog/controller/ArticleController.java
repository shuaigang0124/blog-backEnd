package com.gsg.blog.controller;

import cn.hutool.core.util.ObjectUtil;
import com.gsg.blog.dto.ArticleDTO;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.utils.*;
import com.gsg.blog.vo.PageResponseVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author shuaigang
 * @date 2022/10/19 15:09
 */
@RestController
@RequestMapping("/gsg/atc")
public class ArticleController {

    @Autowired
    ESearchUtils eSearchUtils;

    @Autowired
    UserMapper userMapper;

    /** 目标索引 */
    String indexName = "article";

    @PostMapping("/insertArticle")
    public Result<?> insertMsg(@RequestBody ArticleDTO articleDTO) {

        if (StringUtils.isEmpty(articleDTO.getUserId())
                || articleDTO.getClassifyId() == null
                || ObjectUtil.isEmpty(articleDTO.getTagIds())
                || StringUtils.isEmpty(articleDTO.getTitle())
                || StringUtils.isEmpty(articleDTO.getContent())
        ) {
            throw ServiceException.errorParams("传入参数有误");
        }

        if (articleDTO.getSort() == null) {
            articleDTO.setSort(0);
        }
        if (articleDTO.getOriginal() == null) {
            articleDTO.setOriginal(0);
        }

        articleDTO.setId("ATC" + PKGenerator.generate())
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

}
