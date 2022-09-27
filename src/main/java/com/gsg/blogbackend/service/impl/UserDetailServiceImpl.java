package com.gsg.blogbackend.service.impl;

import com.gsg.blogbackend.ex.ServiceException;
import com.gsg.blogbackend.model.JwtUserDetails;
import com.gsg.blogbackend.model.User;
import com.gsg.blogbackend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static com.gsg.blogbackend.utils.Constants.DELIMITER_3;

/**
 * @Description: security默认实现类
 * @Author shuaigang
 * @Date 2021/9/29 14:48
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private static final String MOBILE = "mobile";
    private static final String ACCOUNT = "account";

    @Autowired
    IUserService userService;

    @Override
    public JwtUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] usernameSplit = username.split(DELIMITER_3);
        username = usernameSplit[0];

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            throw ServiceException.errorParams("程序内部错误, 请联系管理员");
        }

        // 后台登录逻辑
        User userDetail;

        /* 1、用户ID校验登录 (暂不做登录使用)*/
        if (username.startsWith("GSG")) {
            userDetail = userService.findUserById(username);
            if (userDetail == null) {
                throw ServiceException.errorParams("未填写有效用户名！");
            }
            return new JwtUserDetails(
                    userDetail.getId(),
                    username,
                    userDetail.getPassword(),
                    userDetail.getRole()
            );
        }

        User phone = null;
        User email = null;
        String[] stringArr = username.split(" ");
        String type = stringArr[0];

        /* 2、手机号登陆 */
        if (MOBILE.equals(type)) {
            phone = userService.findUserByPhone(stringArr[1]);
        }

        /* 3、邮箱登录 */
        if (ACCOUNT.equals(type)) {
            email = userService.findUserByEmail(stringArr[1]);
        }

        // 判断登录
        if (phone == null && email == null) {
            throw ServiceException.errorParams("未填写有效用户名！");
        }

        // 手机号登陆
        if (phone != null) {
            String newPhone = type + " " + phone.getPhone();
            return new JwtUserDetails(
                    phone.getId(),
                    newPhone,
                    phone.getPassword(),
                    phone.getRole()
            );
        }

        // 邮箱登录
        String newEmail = type + " " + email.getEmail();
        return new JwtUserDetails(
                email.getId(),
                newEmail,
                email.getPassword(),
                email.getRole()
        );

    }
}
