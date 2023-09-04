package com.gsg.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.model.AppVersion;
import com.gsg.blog.service.IAppVersionService;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/9/4 9:09
 */
@RestController
@RequestMapping("/gsg/appVersion")
public class AppVersionController extends BaseController {

    @Autowired
    IAppVersionService appVersionService;

    @PostMapping("/insert")
    public Result<?> insert(@RequestBody AppVersion appVersion) {
        boolean row = appVersionService.save(appVersion);
        if (!row) {
            throw ServiceException.busy();
        }
        return result(R.ok());
    }

    @PostMapping("/getLatest")
    public Result<?> update() {
        LambdaQueryWrapper<AppVersion> qw = new LambdaQueryWrapper<>();
        qw.last("LIMIT 1").orderByDesc(AppVersion::getVersionCode);
        return result(R.ok(appVersionService.getOne(qw)));
    }

    @PostMapping("/getList")
    public Result<?> getList(@RequestBody AppVersion appVersion) {
        LambdaQueryWrapper<AppVersion> qw = new LambdaQueryWrapper<>();
        Page<AppVersion> page = new Page<>(appVersion.getPageNum(), appVersion.getPageSize());
        qw.like(StringUtils.isNotEmpty(appVersion.getVersion()), AppVersion::getVersion, appVersion.getVersion());
        qw.orderByAsc(AppVersion::getVersionCode);
        return result(R.ok(appVersionService.page(page, qw).getRecords()));
    }

}
