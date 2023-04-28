package com.gsg.blog.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 操作Redis服务器中的数据工具类
 * @author shuaigang
 * @date  2023/4/28 19:47
 */
@Component
public class RedisUtils {

    @Resource
    RedisTemplate<String, Serializable> redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public void setList(String key, String value) {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        opsForList.leftPush(key, value);
    }

    public List<String> getList(String key) {
        ListOperations<String, String> ops = stringRedisTemplate.opsForList();
        return ops.range(key, 0, -1);
    }

    public Long listSize(String key) {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        return opsForList.size(key);
    }

    public Long getTimeStamp(String key) {
        ListOperations<String, String> opsForList = stringRedisTemplate.opsForList();
        return Long.parseLong(opsForList.rightPop(key));
    }

    /**
     * 设置带有超时时间的key和value
     * @param timeout 超时时间
     */
    public void set(String key, String value, long timeout){
        ValueOperations<String, String> vo = stringRedisTemplate.opsForValue();
        vo.set(key, value, timeout, TimeUnit.MINUTES);
    }


    /**
     * 设置带有超时时间的key和value（超时时间单位为秒）
     * @param timeout 超时时间
     */
    public void setWithTimeoutSeconds(String key, String value, long timeout){
        ValueOperations<String, String> vo = stringRedisTemplate.opsForValue();
        vo.set(key, value, timeout, TimeUnit.SECONDS);
    }


    public String getString(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 删除key-value
     * @param key key值
     */
    public void remove(String key){
        stringRedisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     * @param key
     */
    public boolean isExist(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 删除redis中的数据
     * @param key 被删除数据的key
     * @return 删除的结果是否成功
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 向Redis中存入数据
     *
     * @param key 在Redis中存入数据时使用的Key
     * @param value 存入的值
     */
    public void set(String key, Serializable value) {
        ValueOperations<String, Serializable> ops = redisTemplate.opsForValue();
        ops.set(key,value);
    }

    /** 修改list中对应下标的参数 */
    public void lset(String key,Integer index,Serializable  value){
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();
        ops.set(key,index,value);
    }

    /**
     * 从Redis中取出数据
     *
     * @param key 此前存入数据时使用的key
     * @return 在Redis中与参数key匹配的值
     */
    public Serializable get(String key) {
        ValueOperations<String, Serializable> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    /**
     * 向redis中的某个List集合添加元素
     * @param key 在redis中集合类型数据的key
     * @param value 需要存入到redis的list集合的元素
     * @return
     */
    public Long rightPush(String key, Serializable value) {
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();
        return ops.rightPush(key, value);
    }

    /**
     * 获取Redis中某List集合
     * @param key 需要获取的数据在Redis中的Key
     * @return List集合
     */
    public List<Serializable> listRange(String key) {
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();
        long start = 0;
        long end = ops.size(key);
        return ops.range(key, start, end);
    }

    /**
     * 获取Redis中某List集合中的数据片段
     * @param key   需要获取的数据在Redis中的Key
     * @param start 获取的数据片段在List集合中的起始位置
     * @param end   获取的数据片段在List集合中的结束位置
     * @return List集合中的数据片段
     */
    public List<Serializable> listRange(String key, long start, long end) {
        ListOperations<String, Serializable> ops = redisTemplate.opsForList();
        return ops.range(key, start, end);
    }

    public Long getExpire(String key){
        Long expire = redisTemplate.getExpire(key);
        return expire;
    }
    
    public void setExpire(String key ,Long time ,TimeUnit timeUnit){
        redisTemplate.expire(key,time,timeUnit);
    }

    public void sopsForValueet(String key ,String value ,Long var3 ,TimeUnit var4){
        redisTemplate.opsForValue().set(key,value,var3,var4);
    }

}
