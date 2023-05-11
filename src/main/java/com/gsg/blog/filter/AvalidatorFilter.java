package com.gsg.blog.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.utils.BaseUtil;
import com.gsg.blog.utils.Constants;
import com.gsg.blog.utils.JacksonUtils;
import com.gsg.blog.utils.ParseJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Description: 过滤器一：判断接口请求是（上传文件、访问图片、数据请求、登录请求）
 * @Author: shuaigang
 * @Date: 2021/9/29 16:15
 */
@Component
@Slf4j
public class AvalidatorFilter extends OncePerRequestFilter {

    private static final String DATA = "data";

    private static final String LOGIN_DATA = "loginData";

    private static final String TYPE = "type";

    private static final String MOBILE = "mobile";

    private static final String ACCOUNT = "account";

    private static final String USERNAME = "username";

    private static final String PASSWORD = "password";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        log.info("AvalidatorFilter过滤器1登录执行开始");

        String contentType = request.getContentType();
        if (contentType != null && contentType.contains(Constants.CONTENT_TYPE)){
            String serverName = request.getHeader("serverName");
            if (ObjectUtils.isEmpty(serverName)) {
                serverName = request.getServerName() + ":" + request.getServerPort();
            }
            if(!ObjectUtils.isEmpty(request.getScheme())){
                serverName = request.getScheme() + "://" + serverName;
            }
            request.setAttribute("serverName", serverName);
            chain.doFilter(request, response);

            log.debug("请求目标地址{}", serverName);
            log.info("文件上传接口，跳过过滤器1 逻辑");
            return;
        }

        MyHttpServletRequestWrapper requestWrapper = new MyHttpServletRequestWrapper(request);
        String replace;
        JSONObject dataJson;
        String body = requestWrapper.getBody();
        if (body == null || "".equals(body)) {
            chain.doFilter(request, response);
        }
        log.debug("解析前的body: {}", body);
        JSONObject bodyJson = JSON.parseObject(body);
        /* 得到base64编码data的value*/
        String data = bodyJson.getString(DATA);
        /* 解码*/
        String dataDecode = BaseUtil.decode(data);
        if (StringUtils.isEmpty(dataDecode)) {
            throw ServiceException.errorParams();
        }

        try {
            Object requestParam = JacksonUtils.json2pojo(dataDecode, Object.class);
            replace = JacksonUtils.obj2json(requestParam);
            if (StringUtils.isEmpty(replace)) {
                log.error("请求参数异常, 请检查!");
            }
            dataJson = JSON.parseObject(replace);
            log.debug("解析后的body: {}", dataJson);

            MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper(request, replace.getBytes());
            Map paramMap = ParseJsonUtil.stringToCollect(dataDecode);

            /* 将解密后的JSON请求参数放入本次 HttpServletRequest*/
            myHttpServletRequestWrapper.setParameterMap(paramMap);

            String loginData = dataJson.getString(LOGIN_DATA);
            if (loginData != null) {
                JSONObject loginDataJson = JSON.parseObject(loginData);
                String password = loginDataJson.getString(PASSWORD);
                String type = dataJson.getString(TYPE);
                String typeAndUserName = null;
                /* 登录类型为手机号*/
                if (MOBILE.equals(type)) {
                    typeAndUserName = type + " " + loginDataJson.getString(USERNAME);
                }
                /* 登录类型为邮箱*/
                if (ACCOUNT.equals(type)) {
                    typeAndUserName = type + " " + loginDataJson.getString(USERNAME);
                }
                myHttpServletRequestWrapper.setParameter(USERNAME, typeAndUserName);
                myHttpServletRequestWrapper.setParameter(PASSWORD, password);
            }

            log.info("过滤器1执行结束");
            /* 放行，把我们的requestWrapper1放到方法当中*/
            chain.doFilter(myHttpServletRequestWrapper, response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("解析失败{}",e.getClass().getName());
        }
    }
}

