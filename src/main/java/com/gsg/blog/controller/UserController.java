package com.gsg.blog.controller;

import cn.hutool.core.util.ObjectUtil;
import com.gsg.blog.dto.RequestDTO;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.service.IUserService;
import com.gsg.blog.utils.*;
import com.gsg.blog.vo.UserDetailsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author shuaigang
 * @date 2022/10/19 14:06
 */
@RestController
@RequestMapping("/gsg/user")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/insertUser")
    public Result<?> test(@RequestBody @Valid UserDTO userDTO) {
        /*检查待新增的用户相关信息是否存在*/
        if (Constants.NOT_UNIQUE.equals(userService.checkUserNameUnique(userDTO))) {
            return Result.ok(BaseUtil.encode(R.failed(new ServiceException("注册用户'" + userDTO.getUserName() + "'失败，用户名已存在！"))));
        }
        if (ObjectUtil.isNotEmpty(userDTO.getEmail())
                && Constants.NOT_UNIQUE.equals(userService.checkEmailUnique(userDTO))) {
            return Result.ok(BaseUtil.encode(R.failed(new ServiceException("注册用户'" + userDTO.getUserName() + "'失败，邮箱账号已存在！"))));
        }

        String userId = "GSG" + PkGenerator.generate();
        userDTO.setId(userId)
                // 对用户密码加密
                .setPassword(PasswordUtils.encode(userDTO.getPassword()));
        userService.insertUser(userDTO);
        return Result.ok(BaseUtil.encode(R.ok("注册成功")));
    }

    /**
     * 通过userId查询用户详情
     *
     * @author gaoshenggang
     * @date 2021/11/26 13:31
     */
    @PostMapping("/getUserDetails")
    public Result<?> getUserDetails(@RequestBody RequestDTO requestDTO) {
        UserDetailsVO userDetailsVO = userService.getUserDetails(requestDTO.getUserId());
        return Result.ok(BaseUtil.encode(R.ok(userDetailsVO)));
    }


}
