package com.yupi.springbootinit.rabbitMq;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.constant.MqConstant;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
@RabbitListener(queues = MqConstant.DEAD_LETTER_QUEUE)
public class DeadConsumer {
    @RabbitHandler
    public void process(Map<String, Object> message, Channel channel, Message mqMsg) throws IOException {
        System.out.println("死信队列收到消息 : " + message.toString());
        channel.basicAck(mqMsg.getMessageProperties().getDeliveryTag(), false);
    }
}
