package com.gsg.blog.mapper;

import com.gsg.blog.model.UserKudos;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户-点赞记录表(目前数据量较少，同时存储博客与评论的点赞记录) Mapper 接口
 * </p>
 *
 * @author shuaigang
 * @since 2023-05-09
 */
@Mapper
public interface UserKudosMapper extends BaseMapper<UserKudos> {

}
