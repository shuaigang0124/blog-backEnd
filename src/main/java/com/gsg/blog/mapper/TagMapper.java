package com.gsg.blog.mapper;

import com.gsg.blog.model.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 标签表 Mapper 接口
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-13
 */
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}
