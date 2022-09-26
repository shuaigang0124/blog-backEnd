package com.gsg.blogbackend.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gsg.blogbackend.utils.BaseUtil;
import com.gsg.blogbackend.utils.JacksonUtils;
import com.gsg.blogbackend.utils.ParseJsonUtil;
import com.gsg.blogbackend.utils.Request;
import jdk.jshell.execution.Util;
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
 * @Description: TODO
 * @Author shuaigang
 * @Date 2021/9/29 16:15
 */
@Component
@Slf4j
public class AvalidatorFilter extends OncePerRequestFilter {

    private static final String DATA = "data";

    public static final String CUSTOM_DATA = "customData";

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
        if (contentType != null && contentType.contains("multipart/form-data")){
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

        try {

            Request<?> requestParam = JacksonUtils.json2pojo(dataDecode, Request.class);
            replace = JacksonUtils.obj2json(requestParam);
            if (StringUtils.isEmpty(replace)) {
                log.error("请求参数异常, 请检查!");
            }
            dataJson = JSON.parseObject(replace);

            String customData = dataJson.getString(CUSTOM_DATA);
            log.debug("解析后的body: {}", customData);
            JSONObject jsonObject = JSON.parseObject(customData);

            MyHttpServletRequestWrapper myHttpServletRequestWrapper = new MyHttpServletRequestWrapper(request, replace.getBytes());
            Map paramMap = ParseJsonUtil.stringToCollect(dataDecode);

            /* 将解密后的JSON请求参数放入本次 HttpServletRequest*/
            myHttpServletRequestWrapper.setParameterMap(paramMap);

            String loginData = jsonObject.getString(LOGIN_DATA);
            if (loginData != null) {
                JSONObject loginDataJson = JSON.parseObject(loginData);
                String password = loginDataJson.getString(PASSWORD);
                String type = jsonObject.getString(TYPE);
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

