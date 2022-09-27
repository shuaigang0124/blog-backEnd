package com.gsg.blogbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsg.blogbackend.model.User;
import org.springframework.stereotype.Repository;

/**
 * @author shuaigang
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据条件查询对应的用户信息（内部使用）
     * @author gaoshenggang
     * @date  2021/11/19 11:05
     */
    User getUserByCondition(User userDTO);

}
