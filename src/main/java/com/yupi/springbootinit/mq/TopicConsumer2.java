package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class TopicConsumer2 {

  private static final String EXCHANGE_NAME = "topic_exchange";

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();


    channel.exchangeDeclare(EXCHANGE_NAME, "topic");
    // 获得匿名通道名
    String queueName = channel.queueDeclare().getQueue();
    // 以后端结尾才能匹配上
    channel.queueBind(queueName, EXCHANGE_NAME, "#.后端");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), "UTF-8");
      channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);
        System.out.println(" [x] Received '" +
            delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
    };
    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
  }
}