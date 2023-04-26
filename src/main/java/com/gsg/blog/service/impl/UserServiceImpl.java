package com.gsg.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.model.ChatMsg;
import com.gsg.blog.model.User;
import com.gsg.blog.service.IChatMsgService;
import com.gsg.blog.service.IUserService;
import com.gsg.blog.utils.Constants;
import com.gsg.blog.utils.DateFormateUtils;
import com.gsg.blog.utils.ESearchUtils;
import com.gsg.blog.utils.WebPUtils;
import com.gsg.blog.vo.UserDetailsVO;
import com.gsg.blog.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author shuaigang
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ESearchUtils eSearchUtils;

    @Autowired
    private IChatMsgService chatMsgService;

    @Value("${rabbitmq.chatExchangeName}")
    String chatExchangeName;

    @Override
    public void insertUser(UserDTO userDTO) {
        userDTO
                .setAvatar("/gsg/static-resource/formal/2/20211218/1639807415900-1622025345981709.jpg")
                .setGmtCreate(new Date())
                .setGmtModified(new Date());
        int i = userMapper.insertUser(userDTO);
        if (i != 1) {
            throw ServiceException.busy();
        }

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userDTO, userVO);
        eSearchUtils.doc.createOrUpdate("user", userVO.getId(), userVO);
        // 订阅聊天
        try {
            ChatMsg chatMsg = new ChatMsg();
            chatMsg.setUserId(userDTO.getId())
                    .setRoomId(chatExchangeName)
                    .setContent("大家好，我是" + userDTO.getUserName() + "。")
                    .setSendTime(DateFormateUtils.formateDate(new Date(), DateFormateUtils.STANDARD_STAMP2));
            chatMsgService.saveMsg(chatMsg);
        } catch (Exception e) {
            log.error("用户订阅聊天失败！");
            e.printStackTrace();
        }
    }

    /**
     * 校验用户名称是否唯一
     */
    @Override
    public String checkUserNameUnique(UserDTO userDTO) {
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
    public String checkPhoneUnique(UserDTO userDTO) {
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
    public String checkEmailUnique(UserDTO userDTO) {

        String userId = StringUtils.isEmpty(userDTO.getId()) ? "GSG1" : userDTO.getId();
        User user = userMapper.checkEmailUnique(userDTO.getEmail());
        if (ObjectUtil.isNotEmpty(user) && !user.getId().equals(userId)) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    /**
     * 根据条件查询对应的用户信息（内部登录使用）
     *
     * @author shuaigang
     * @date 2021/11/19 11:13
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

    @Override
    public UserDetailsVO getUserDetails(String userId) {
        if (StringUtils.isEmpty(userId)) {
            throw ServiceException.errorParams("userId不能为空");
        }
        User user = userMapper.getUserById(userId);
        UserDetailsVO userDetailsVO = new UserDetailsVO();
        if (!StringUtils.isEmpty(user.getAvatar())) {
            String avatarPath = user.getAvatar();
            // 20211210 生成WebP图片副本
            avatarPath = WebPUtils.changePathToWebp("1", avatarPath);
            user.setAvatar(avatarPath);
        }
        userDetailsVO.setId(user.getId())
                .setUserName(user.getUserName())
                .setBirthday(user.getBirthday())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setAvatar(user.getAvatar())
                .setSex(user.getSex())
                .setAddress(user.getAddress());

        return userDetailsVO;
    }

}
