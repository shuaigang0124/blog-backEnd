package com.gsg.blog.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gsg.blog.dto.CommonDto;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.model.Tag;
import com.gsg.blog.service.ITagService;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 标签表 前端控制器
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-13
 */
@RestController
@RequestMapping("/gsg/tag")
public class TagController extends BaseController {

    @Autowired
    private ITagService tagService;

    @PostMapping("/insert")
    public Result<?> insert(@RequestBody Tag tag) {
        boolean row = tagService.save(tag);
        if (!row) {
            throw ServiceException.busy();
        }
        return result(R.ok());
    }

    @PostMapping("/update")
    public Result<?> update(@RequestBody Tag tag) {
        boolean row = tagService.updateById(tag);
        if (!row) {
            throw ServiceException.busy();
        }
        return result(R.ok());
    }

    @PostMapping("/getList")
    public Result<?> getList(@RequestBody Tag tag) {
        LambdaQueryWrapper<Tag> qw = new LambdaQueryWrapper<>();
        qw.like(StringUtils.isNotEmpty(tag.getName()), Tag::getName, tag.getName());
        return result(R.ok(tagService.list(qw)));
    }

}
