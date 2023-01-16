package com.gsg.blog.controller;

import cn.hutool.core.util.ObjectUtil;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.service.IUserService;
import com.gsg.blog.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

/**
 *
 * @author shuaigang
 * @date 2022/10/19 14:06
 */
@RestController
@RequestMapping("/gsg/user")
public class UserController {

    @Autowired
    IUserService userService;

    @PostMapping("/insertUser")
    public Result<?> test(@RequestBody @Valid Request<UserDTO> request, BindingResult bindingResult) {
        if (ValidErrorUtil.hasError(bindingResult) != null) {
            return ValidErrorUtil.hasError(bindingResult);
        }
        UserDTO userDTO = request.getCustomData();
        /*入参校验*/
        Assert.notNull(userDTO.getUserName(),"参数异常，userName 必传");
        Assert.notNull(userDTO.getPassword(),"参数异常，password 必传");
        Assert.notNull(userDTO.getBirthday(),"参数异常，birthday 必传");
        Assert.notNull(userDTO.getEmail(),"参数异常，email 必传");
        Assert.notNull(userDTO.getPhone(),"参数异常，phone 必传");
        Assert.notNull(userDTO.getSex(),"参数异常，sex 必传");
        Assert.notNull(userDTO.getAddress(),"参数异常，address 必传");
        /*检查待新增的用户相关信息是否存在*/
        if (Constants.NOT_UNIQUE.equals(userService.checkUserNameUnique(userDTO))) {
            return Result.ok(BaseUtil.encode(R.failed(new ServiceException("注册用户'" + userDTO.getUserName() + "'失败，用户名已存在！"))));
        }
//            if (!StringUtils.isEmpty(userDTO.getPhone())
//                    && Constants.NOT_UNIQUE.equals(userService.checkPhoneUnique(userDTO))) {
//                return R.failed(new ServiceException("注册用户'" + userDTO.getUserName() + "'失败，手机号码已存在！"));
//            }
        if (ObjectUtil.isNotEmpty(userDTO.getEmail())
                && Constants.NOT_UNIQUE.equals(userService.checkEmailUnique(userDTO))) {
            return Result.ok(BaseUtil.encode(R.failed(new ServiceException("注册用户'" + userDTO.getUserName() + "'失败，邮箱账号已存在！"))));
        }

        String userId = "GSG" + PKGenerator.generate();
        userDTO.setId(userId)
                // 对用户密码加密
                .setPassword(PasswordUtils.encode(userDTO.getPassword()));
        userService.insertUser(userDTO);
        return Result.ok(BaseUtil.encode(R.ok("注册成功")));
    }


}
