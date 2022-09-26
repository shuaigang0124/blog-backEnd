package com.gsg.blogbackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Description Base64 加密工具类
 **/
@Slf4j
public class BaseUtil {

    //“盐”
//    private static final String SALT = "neusoft";
    //利用“盐”值加密5遍数据
//    private static final int REPEAT = 1;

    /**
     * @Description Base64 加密
     **/
    public static String encode(Object r) {
        try {
            String result = JacksonUtils.obj2json(r);
            byte[] data = result.getBytes();
            data = Base64.getEncoder().encode(data);
            return new String(data, StandardCharsets.UTF_8);
        } catch (JsonProcessingException e) {
            log.error("Jackson出现异常>>>>>", e);
        } catch (Exception e) {
            log.error("加密出现异常>>>>>", e);
        }
        return null;
    }

    /**
     * @Description Base64 解密
     **/
    public static String decode(String data) throws UnsupportedEncodingException {

        byte[] bytes = Base64.getDecoder().decode(data);
        return new String(bytes, StandardCharsets.UTF_8);
    }

}
