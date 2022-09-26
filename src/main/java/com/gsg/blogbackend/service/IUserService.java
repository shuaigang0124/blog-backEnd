package com.gsg.blogbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gsg.blogbackend.model.User;

public interface IUserService extends IService<User> {

    /**
     * 以下接口用于内部后台的登录使用
     * @author gaoshenggang
     * @date  2021/11/15 14:02
     */

    User findUserById(String id);

    User findUserByPhone(String phone);

    User findUserByEmail(String email);

}
