package com.gsg.blog.service.impl;

import com.gsg.blog.model.Tag;
import com.gsg.blog.mapper.TagMapper;
import com.gsg.blog.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-13
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

}
