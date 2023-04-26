package com.gsg.blog.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * TODO RabbitMQ配置类
 * @author shuaigang
 * @date 2022/8/31 0019 下午 02:08:020
 * @version 1.0
 **/
@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
@Component
@ConfigurationProperties(prefix = "rabbitmq")
@Configuration
public class RabbitMQConfig {

    /** RabbitMQ的连接地址 */
    private String url;
    /** RabbitMQ的连接端口 */
    private Integer port;
    /** RabbitMQ连接用户名 */
    private String username;
    /** RabbitMQ连接密码 */
    private String password;
    /** 断线重连间隔时间*/
    private Integer networkRecoveryInterval;

    @Bean
    public RabbitMqChatClient getRabbitMqChatClient(){

        RabbitMqChatClient rabbitMqChatClient = new RabbitMqChatClient();

        rabbitMqChatClient.connect(url, port, username, password, networkRecoveryInterval);

        /** 方便在启动类中进行监听队列设置*/
        setRabbitMqChatClient(rabbitMqChatClient);

        return rabbitMqChatClient;
    }

    public static RabbitMqChatClient rabbitMqChatClient;
    public void setRabbitMqChatClient(RabbitMqChatClient rabbitMqChatClient) {
        RabbitMQConfig.rabbitMqChatClient = rabbitMqChatClient;
    }

    /** 自定义exchange */
    public static String chatExchangeName;
    @Value("${rabbitmq.chatExchangeName}")
    public void setChatExchangeName(String chatExchangeName) {
        RabbitMQConfig.chatExchangeName = chatExchangeName;
    }

    /** 超时时间 ttl 毫秒 */
    public static String ttl;
    @Value("${rabbitmq.ttl}")
    public void setTtl(String ttl) {
        RabbitMQConfig.ttl = ttl;
    }

    /** 某些自动回复控制一定时间内部进行重复提醒，单位秒 */
    public static Long autoReplyTimeLimit;
    @Value("${chat.autoReplyTimeLimit}")
    public void setAutoReplyTimeLimit(Long autoReplyTimeLimit) {
        RabbitMQConfig.autoReplyTimeLimit = autoReplyTimeLimit;
    }

}
