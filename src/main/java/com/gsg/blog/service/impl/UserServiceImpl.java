package com.gsg.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.model.User;
import com.gsg.blog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shuaigang
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    /**
     * 根据条件查询对应的用户信息（内部登录使用）
     * @author gaoshenggang
     * @date  2021/11/19 11:13
     */

    @Override
    public User findUserById(String id) {
        User user = new User();
        user.setId(id);
        return userMapper.getUserByCondition(user);
    }

    @Override
    public User findUserByPhone(String phone) {
        User user = new User();
        user.setPhone(phone);
        return userMapper.getUserByCondition(user);
    }

    @Override
    public User findUserByEmail(String email) {
        User user = new User();
        user.setEmail(email);
        return userMapper.getUserByCondition(user);
    }

}
