package com.gsg.blog.handler;

import com.gsg.blog.utils.BaseUtil;
import com.gsg.blog.utils.JacksonUtils;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description: 自定义无权限访问处理类
 * @Author shuaigang
 * @Date 2021/9/29 12:58
 */
@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.debug("权限不足！");
        String json = JacksonUtils.obj2json(Result.ok(BaseUtil.encode(R.construct(R.FORBIDDEN,"权限不足！"))));
        // 指定相应格式为json
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }
}
