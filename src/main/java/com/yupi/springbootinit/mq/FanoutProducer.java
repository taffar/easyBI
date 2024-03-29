package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class FanoutProducer {

  private static final String EXCHANGE_NAME = "logs";

    public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    try (Connection connection = factory.newConnection();
         Channel channel = connection.createChannel()) {
        // 声明队列(绑定到不同的队列
        channel.queueDeclare("", false, false, false, null);
        // 获取匿名队列名称
        String QUEUE_NAME = channel.queueDeclare().getQueue();
        // 交换机声明
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 队列与交换机绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.nextLine();
            channel.basicPublish(EXCHANGE_NAME, QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
  }
}