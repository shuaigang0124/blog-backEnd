package com.gsg.blog.controller;

import com.gsg.blog.config.JwtProperties;
import com.gsg.blog.dto.RequestDTO;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.model.JwtUserDetails;
import com.gsg.blog.service.impl.UserDetailServiceImpl;
import com.gsg.blog.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 登录控制器
 * @author shuaigang
 * @date 2021/9/29 17:22
 */
@RestController
@RequestMapping("/gsg/authentication")
@Component
public class LoginController {

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    RedisUtils redisUtils;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtProperties jwtProperties;

    @PostMapping("/form")
    public void login(String username, String password) {
        // TODO 这里面不需要写任何代码，由UserDetailsService去处理
    }

    /**
     * 根据用户id生成新的token令牌
     */
    @PostMapping("/generateToken")
    public Result<?> generateToken(@RequestBody @Valid RequestDTO requestDTO, BindingResult bindingResult) {
        if (requestDTO.getUserId() == null) {
            throw ServiceException.errorParams();
        }
        String userId = requestDTO.getUserId();
        JwtUserDetails userDetails = userDetailService.loadUserByUsername(userId);
        redisUtils.set(userId, "");
        String newToken = jwtTokenUtil.generateToken(userId);

        Map<String, Object> map = new HashMap<>(5);

        map.put("Authorization", newToken);
        map.put("userId", userId);
        map.put("role", userDetails.getRole());
        return Result.ok(BaseUtil.encode(R.ok(map)));
    }
}
