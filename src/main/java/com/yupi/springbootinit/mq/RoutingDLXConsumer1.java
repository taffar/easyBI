package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class RoutingDLXConsumer1 {

    private static final String EXCHANGE_NAME = "routing_exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        // 指定死信参数
        Map<String, Object> args = new HashMap<String, Object>();
        // 指定死信绑定到哪个交换机
        args.put("x-dead-letter-exchange", "dltExchange");
        // 指定死信要转发到哪个死信队列
        args.put("dead-letter-routing-key", "waiBao");
        // 该队列绑定死信交换机与死信路由
        String queueName = "backend_queue";
        channel.queueDeclare(queueName, false, false, false, args);
        // 该队列绑定工作队列与工作路由
        channel.queueBind(queueName, EXCHANGE_NAME, "backend");

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
        };
        // 监听匿名工作消息队列
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    }
}