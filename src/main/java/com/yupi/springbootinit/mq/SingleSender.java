package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

/**
 * Created by lily via on 2024/3/28 15:17
 */
public class SingleSender {
        private final static String QUEUE_NAME = "hello";
        public static void main(String[] argv) throws Exception {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {
                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()){
                    String message = scanner.nextLine();
                    channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                    System.out.println(" [x] Sent '" + message + "'");
                }
            }
        }

}
