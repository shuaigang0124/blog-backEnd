package com.gsg.blog.utils;


/**
 * @author shuaigang
 * @date  2021/9/29 15:38
 */
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 处理密码加密相关任务的工具类
 * @author shuaigang
 * @date  2023/4/28 19:35
 */
public class PasswordUtils {

    /**
     * 密码加密器
     */
    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public static String passwordSalt ="{bcrypt}";
    /**
     * 执行密码加密
     * @param rawPassword 密码原文
     * @return 加密结果
     */
    public static String encode(String rawPassword) {
        return passwordSalt+passwordEncoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        encodedPassword = encodedPassword.split("}")[1];
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
