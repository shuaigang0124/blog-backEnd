package com.gsg.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.model.User;
import com.gsg.blog.vo.UserDetailsVO;

public interface IUserService extends IService<User> {

    void insertUser(UserDTO userDTO);

    /**
     * 校验用户名称是否唯一
     */
    String checkUserNameUnique(UserDTO userDTO);

    /**
     * 校验用户电话号码是否唯一
     */
    String checkPhoneUnique(UserDTO userDTO);

    /**
     * 校验用户邮箱是否唯一
     */
    String checkEmailUnique(UserDTO userDTO);

    /**
     * 以下接口用于内部后台的登录使用
     * @author shuaigang
     * @date  2021/11/15 14:02
     */

    User findUserById(String id);

    User findUserByPhone(String phone);

    User findUserByEmail(String email);

    UserDetailsVO getUserDetails(String userId);
}
