package com.gsg.blog.controller;

import com.gsg.blog.config.JwtProperties;
import com.gsg.blog.dto.RequestDTO;
import com.gsg.blog.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;


/**
 * @author shuaigang
 * @date  2021/9/29 17:43
 */
@RestController
@RequestMapping("/gsg/authentication")
@Slf4j
public class SecurityController {

    @Autowired
    RedisUtils redisUtils;

    @Resource
    private JwtProperties jwtProperties;

    private final RequestCache requestCache = new HttpSessionRequestCache();

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @PostMapping("/Login")
    @ResponseStatus(code = HttpStatus.UNAUTHORIZED)
    public Result<?> requireAuthentication (HttpServletRequest request, HttpServletResponse response) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        return Result.ok(BaseUtil.encode(R.ok("未认证!")));
    }

    @PostMapping("/logout")
    public Result<?> loginOut(@RequestBody @Valid RequestDTO requestDTO, HttpServletRequest httpServletRequest){
        String userId = requestDTO.getUserId();

        try {
            redisUtils.delete(userId);
            log.debug("[{}]已退出!" + userId);
            return Result.ok(BaseUtil.encode(R.ok("退出成功！")));
        } catch (Exception e) {
            log.error("未退出!{}", e.getClass().getName());
            e.printStackTrace();
            return Result.ok(BaseUtil.encode(R.ok("退出失败！")));
        }
    }

}
