package com.gsg.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.mapper.AppVersionMapper;
import com.gsg.blog.model.AppVersion;
import com.gsg.blog.service.IAppVersionService;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author shuaigang
 * @date 2023/9/4 9:29
 */
@Service
public class AppVersionServiceImpl extends ServiceImpl<AppVersionMapper, AppVersion> implements IAppVersionService {
}
