package com.yupi.springbootinit.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class RoutingDLXProducer {

  private static final String EXCHANGE_NAME = "routing_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String s = scanner.nextLine();
            String[] splits = s.split(" ");
            if (splits.length<2) continue;
            String routingKey = splits[0];
            String message = splits[1];
            // 设置消息的过期时间
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .expiration("300")
                    .build();
            channel.basicPublish(EXCHANGE_NAME, routingKey, properties, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        };
    }
  }
}