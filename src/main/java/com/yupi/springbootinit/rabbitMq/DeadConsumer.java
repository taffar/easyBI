package com.yupi.springbootinit.rabbitMq;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.constant.MqConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
@Slf4j
public class DeadConsumer {
    @RabbitListener(queues = {MqConstant.DEAD_LETTER_QUEUE})
    public void process(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        System.out.println("DeadConsumer...");
        // todo 打日志，报警
        log.info("死信队列收到消息 : " + message);
        channel.basicAck(deliveryTag, false);
    }
}
