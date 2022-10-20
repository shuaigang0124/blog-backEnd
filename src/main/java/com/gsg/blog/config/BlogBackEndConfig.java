package com.gsg.blog.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.unit.DataSize;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import javax.servlet.MultipartConfigElement;
import java.io.Serializable;

/**
 * @author shuaigang
 * @Date: 2022年9月27日  10点41分
 */
@Configuration
public class BlogBackEndConfig implements WebMvcConfigurer {

    /** 配置文件上传接口对文件传输大小的限制*/
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(500));
        factory.setMaxRequestSize(DataSize.ofMegabytes(500));
        return factory.createMultipartConfig();
    }

    /**
     *  redisTemplate 配置
     *  redis-key序列化：解决redis检查key是否存在一直返回false的问题
     */
    @Bean
    public RedisTemplate<String, Serializable> redisTemplate(LettuceConnectionFactory connectionFactory) {
        RedisTemplate<String, Serializable> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return redisTemplate;
    }

}
