package com.gsg.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.model.User;
import com.gsg.blog.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author shuaigang
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 新增用户
     * @param userDTO 用户信息
     * @return  int
     */
    int insertUser(UserDTO userDTO);

    /**
     * 查询用户详情
     * @param userId userId
     * @return user
     */
    User getUserById(String userId);

    /**
     * 查询所用用户
     * @return  list
     */
    List<UserVO> getAllUser();

    /**
     * 校验用户名称是否唯一
     * @param userName  userName
     * @return user
     */
    User checkUserNameUnique(String userName);

    /**
     * 校验手机号码是否唯一
     * @param phone phone
     * @return user
     */
    User checkPhoneUnique(String phone);

    /**
     * 校验email是否唯一
     * @param email email
     * @return  user
     */
    User checkEmailUnique(String email);


    /**
     * 根据条件查询对应的用户信息（内部使用）
     * @param user  user
     * @return  user
     */
    User getUserByCondition(User user);

}
