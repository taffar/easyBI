package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

public class RoutingDLXConsumer2 {

  private static final String EXCHANGE_NAME = "routing_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "direct");
    Map<String, Object> args = new HashMap<>();
    // 声明队列时绑定了死信交换机
//    args.put("x-dead-letter-exchange", "worker");
    channel.queueDeclare("myqueue2", false, false, false, args);
    // 队列又通过一个路由键绑定了一个与生产者连接的交换机
    channel.queueBind("myqueue2", EXCHANGE_NAME, "后端");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
        System.out.println(" [x] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume("myqueue2", true, deliverCallback, consumerTag -> { });
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
  }
}