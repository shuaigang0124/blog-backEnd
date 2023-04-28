package com.gsg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.model.User;
import com.gsg.blog.vo.UserDetailsVO;

/**
 * @author shuaigang
 * @date  2023/4/28 19:34
 */
public interface IUserService extends IService<User> {

    /**
     * 新增用户
     * @param userDTO 用户信息
     */
    void insertUser(UserDTO userDTO);

    /**
     * 校验用户名称是否唯一
     * @param userDTO   用户信息
     * @return  String
     */
    String checkUserNameUnique(UserDTO userDTO);

    /**
     * 校验用户电话号码是否唯一
     * @param userDTO 用户信息
     * @return string
     */
    String checkPhoneUnique(UserDTO userDTO);

    /**
     * 校验用户邮箱是否唯一
     * @param userDTO 用户信息
     * @return  string
     */
    String checkEmailUnique(UserDTO userDTO);

    /**
     * 登录使用-查询用户
     * @param id    userId
     * @return  user
     */
    User findUserById(String id);

    /**
     * 登录使用-查询用户
     * @param phone phone
     * @return  user
     */
    User findUserByPhone(String phone);

    /**
     * 登录使用-查询用户
     * @param email email
     * @return  user
     */
    User findUserByEmail(String email);

    /**
     * 登录使用-查询用户
     * @param userId userId
     * @return user
     */
    UserDetailsVO getUserDetails(String userId);
}
