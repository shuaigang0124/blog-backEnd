package com.gsg.blog.handler;

import com.gsg.blog.model.JwtUserDetails;
import com.gsg.blog.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 自定义认证成功处理器（关键）
 * 当用户认证成功之后，我们要在这里为用户生成token,并返回给用户，需要用到我们自定义的jwt工具类，也需要在配置类中配置
 * @Author shuaigang
 * @Date 2021/9/29 14:25
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 指定相应格式为json
        response.setContentType("application/json;charset=UTF-8");
        JwtUserDetails userDetails = (JwtUserDetails) authentication.getPrincipal();
        // 使用userid生成token
        String userId = userDetails.getUsername();
        String realToken = jwtTokenUtil.generateToken(authentication.getName());
        Map<String, Object> map = new HashMap<>();
        // base64 加密用户Id，用户角色
        String base64UserId = Base64.getEncoder().encodeToString(userId.getBytes());
        String base64Role = Base64.getEncoder().encodeToString(userDetails.getRole().getBytes());

        //Base64 解密方法
//        String userId = new String(Base64.getDecoder().decode(base64UserId));

        map.put("Authorization", realToken);
        map.put("userId", base64UserId);
        map.put("role", base64Role);

//        /* token交由redis管理时效*/
//        redisUtils.setWithTimeoutSeconds(realToken, "", jwtProperties.getTokenValidityInSeconds());

        /**
         * Token副本校验
         * 在redis中存储token副本，用户请求时候校验，如果redis中不存在该副本则不给通过。
         */
        redisUtils.delete(userId);
        redisUtils.set(userId, "");

        //将生成的authentication放入容器中，生成安全的上下文
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 返回数据
        String json = JacksonUtils.obj2json(Result.ok(BaseUtil.encode(R.ok(map))));

        response.getWriter().write(json);

    }
}
