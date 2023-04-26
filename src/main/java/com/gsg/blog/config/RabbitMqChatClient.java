package com.gsg.blog.config;

import com.alibaba.fastjson.JSONObject;
import com.gsg.blog.utils.BaseUtil;
import com.gsg.blog.utils.Constants;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author shuaigang
 * @date 2022/8/31 13:07
 */
@Slf4j
@Component
public class RabbitMqChatClient {

    private static Connection rabbitMqConnection;

    public static Connection getRabbitMqConnection() {
        return rabbitMqConnection;
    }

    public static void setRabbitMqConnection(Connection rabbitMqConnection) {
        RabbitMqChatClient.rabbitMqConnection = rabbitMqConnection;
    }

    String exchangeName = RabbitMQConfig.chatExchangeName;
    String msgTtl = RabbitMQConfig.ttl;

    List<Object> messageResult = null;

    public void connect(String host, Integer port, String username, String password, int networkRecoveryInterval) {

        Connection connection;
        try {
            //创建一个与MQ的连接
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(host);
            factory.setPort(port);
            factory.setUsername(username);
            factory.setPassword(password);
            factory.setAutomaticRecoveryEnabled(true);
            factory.setNetworkRecoveryInterval(networkRecoveryInterval);
            //rabbitmq默认虚拟机名称为“/”，虚拟机相当于一个独立的mq服务器
            factory.setVirtualHost("/");

            /* 创建一个连接*/
            connection = factory.newConnection();
            /* 将连接设置到静态变量中*/
            RabbitMqChatClient.setRabbitMqConnection(connection);

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    /**
     * 消息发送
     */
    public void publish(String queueName, String message) {

        Connection rabbitMqConnection = RabbitMqChatClient.getRabbitMqConnection();

        Channel channel = null;
        try {
            /* 创建与交换机的通道，每个通道代表一个会话*/
            channel = rabbitMqConnection.createChannel();

            /*
             * 声明交换机 String exchange, BuiltinExchangeType type
             * 参数明细
             * 1、交换机名称 2、交换机类型，fanout、topic、direct、headers
             */
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.FANOUT, true);

            /*
             * 声明队列
             * 参数明细：
             * 1、队列名称 2、是否持久化 3、是否独占此队列 4、队列不用是否自动删除 5、参数（设置队列的TTL）
             */
            HashMap<String, Object> paramMap = new HashMap<>();
//            paramMap.put("x-message-ttl", Long.parseLong(msgTtl));
            paramMap.put("x-expires", Long.parseLong(msgTtl));
            channel.queueDeclare(queueName, true, false, false, paramMap);

            /*
             * 交换机和队列绑定String queue, String exchange, String routingKey
             * 参数明细
             * 1、队列名称 2、交换机名称 3、路由key 4、参数
             */
            channel.queueBind(queueName, exchangeName, queueName, null);

            /*
             * 向交换机发送消息 String exchange, String routingKey, BasicProperties props, byte[] body
             * 参数明细
             * 1、交换机名称，不指令使用默认交换机名称 Default Exchange
             * 2、routingKey（路由key），根据key名称将消息转发到具体的队列，这里填写队列名称表示消息将发到此队列
             * 3、消息属性 (设置单条消息消息的ttl)
             * 4、消息内容
             */
            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
            builder.expiration(msgTtl);
            AMQP.BasicProperties build = builder.build();

            channel.basicPublish(exchangeName, queueName, build, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 消息接收
     */
    public List<Object> getMsg(String queueName) {

        Connection rabbitMqConnection = RabbitMqChatClient.getRabbitMqConnection();

        Channel channel = null;
        messageResult = new ArrayList<>();
        try {
            /* 创建与交换机的通道，每个通道代表一个会话*/
            channel = rabbitMqConnection.createChannel();

            channel.basicConsume(queueName, true, new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    // 处理消息获取data数据
                    String decodeData = BaseUtil.decode(((JSONObject) JSONObject.parse(new String(body))).getString("data"));
                    messageResult.add(JSONObject.parse(decodeData));
                    System.out.println("接收到消息：【" + decodeData + "】");
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        }
        return messageResult;
    }

}
