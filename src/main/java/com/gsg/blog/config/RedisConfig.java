package com.gsg.blog.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

/**
 * TODO 修复redisKey序列化问题（redis检查不到key，返回false）
 *
 * @author shuaigang
 * @date 2022/10/20 9:15
 */
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

//    @Bean
//
//    public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
//
//        StringRedisTemplate template = new StringRedisTemplate(factory);
//
//        //定义key序列化方式
//
//        //RedisSerializer redisSerializer = new StringRedisSerializer();//Long类型会出现异常信息;需要我们上面的自定义key生成策略，一般没必要
//
//        //定义value的序列化方式
//
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//
//        ObjectMapper om = new ObjectMapper();
//
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//
//        // template.setKeySerializer(redisSerializer);
//
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        template.afterPropertiesSet();
//
//        return template;
//
//    }

}
