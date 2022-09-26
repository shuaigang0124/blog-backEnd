package com.gsg.blogbackend.handler;

import com.gsg.blogbackend.utils.JacksonUtils;
import com.gsg.blogbackend.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Description: 自定义认证登录失败处理类
 * 当用户输入错误的账号或者密码时，就会进入这个处理类，同样要在配置类中指明
 * @Author shuaigang
 * @Date 2021/9/29 12:51
 */
@Component
@Slf4j
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        log.debug("登陆失败！用户名或密码错误！");
        String json = JacksonUtils.obj2json(R.construct(R.WRONG_PASSWORD, "登陆失败！用户名或密码错误！"));
        // 指定响应格式为json
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
