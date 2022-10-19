package com.gsg.blog.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.model.User;
import com.gsg.blog.service.IUserService;
import com.gsg.blog.utils.Constants;
import com.gsg.blog.utils.DateFormateUtils;
import com.gsg.blog.utils.ESearchUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;

/**
 * @author shuaigang
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    ESearchUtils eSearchUtils;

    @Override
    public void insertUser(UserDTO userDTO) {
        userDTO
                .setAvatar("/gsg/static-resource/formal/2/20211218/1639807415900-1622025345981709.jpg")
                .setGmtCreate(DateFormateUtils.asLocalDateTime(new Date()))
                .setGmtModified(DateFormateUtils.asLocalDateTime(new Date()));
        int i = userMapper.insertUser(userDTO);
        if (i != 1) {
            throw ServiceException.busy();
        }
        eSearchUtils.doc.createOrUpdate("user", userDTO.getId(), userDTO);
    }

    /**
     * 校验用户名称是否唯一
     */
    @Override
    public String checkUserNameUnique(UserDTO userDTO)
    {
        String userId = StringUtils.isEmpty(userDTO.getId()) ? "GSG1" : userDTO.getId();
        User user = userMapper.checkUserNameUnique(userDTO.getUserName());
        if (ObjectUtil.isNotEmpty(user) && !user.getId().equals(userId)) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 校验用户手机号是否唯一
     */
    @Override
    public String checkPhoneUnique(UserDTO userDTO)
    {
        String userId = StringUtils.isEmpty(userDTO.getId()) ? "GSG1" : userDTO.getId();
        User user = userMapper.checkPhoneUnique(userDTO.getPhone());
        if (ObjectUtil.isNotEmpty(user) && !user.getId().equals(userId)) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 校验email是否唯一
     */
    @Override
    public String checkEmailUnique(UserDTO userDTO)
    {

        String userId = StringUtils.isEmpty(userDTO.getId()) ? "GSG1" : userDTO.getId();
        User user = userMapper.checkEmailUnique(userDTO.getEmail());
        if (ObjectUtil.isNotEmpty(user) && !user.getId().equals(userId)) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

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
