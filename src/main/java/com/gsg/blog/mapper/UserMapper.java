package com.gsg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.model.User;
import org.springframework.stereotype.Repository;

/**
 * @author shuaigang
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    /** 增加 */
    int insertUser(UserDTO userDTO);

    /** 查询用户详情 */
    User getUserById(String userId);

    /**
     * 校验用户名称是否唯一
     */
    User checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     */
    User checkPhoneUnique(String phone);

    /**
     * 校验email是否唯一
     */
    User checkEmailUnique(String email);


    /**
     * 根据条件查询对应的用户信息（内部使用）
     * @author gaoshenggang
     * @date  2021/11/19 11:05
     */
    User getUserByCondition(User user);

}
