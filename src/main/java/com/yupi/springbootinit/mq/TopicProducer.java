package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class TopicProducer {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        Scanner scanner = new Scanner(System.in);
        System.out.println(" [x] Sent form: [routing message]");
        while (scanner.hasNext()){
            String s = scanner.nextLine();
            String[] splits = s.split(" ");
            if (splits.length<2) continue;
            String routingKey = splits[0];
            String message = splits[1];
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        };
    }
  }
}