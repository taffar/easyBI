package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class SingleConsumer {

  private final static String QUEUE_NAME = "hello";

  public static void main(String[] argv) throws Exception {
    // open a connection
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    // create a channel
    Channel channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
    // 接收消息处理工作
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      // delivery中包含消息体
      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [worker1] Received '" + message + "'");
    };
    //
    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
  }
}
