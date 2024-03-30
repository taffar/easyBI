package com.yupi.springbootinit.config;

import com.yupi.springbootinit.constant.MqConstant;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: Rabbitmq相关配置类
 * @author Lily Via
 * @date 2024/3/31 1:29
 * @version 1.0
 */
@Configuration
public class RabbitmqConfig {


    /**
     * 正常工作交换机，开启持久化
     */
    @Bean
    DirectExchange normalExchange() {
        return new DirectExchange(MqConstant.BI_WORK_EXCHANGE, true, false);
    }

    @Bean
    public Queue normalQueue() {
        // durable: 是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive: 默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete: 是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        Map<String, Object> args = deadQueueArgs();
        // 队列设置最大长度
        args.put("x-max-length", 100);
        return new Queue(MqConstant.BI_WORK_QUEUE, true, false, false, args);
    }

    @Bean
    public Queue ttlQueue() {
        Map<String, Object> args = deadQueueArgs();
        // 队列设置消息过期时间 60 秒
        args.put("x-message-ttl", 60 * 1000);
        return new Queue("ttlQueue", true, false, false, args);
    }

    @Bean
    Binding normalRouteBinding() {
        return BindingBuilder.bind(normalQueue()).to(normalExchange()).with("normalRouting");
    }

    @Bean
    Binding ttlRouteBinding() {
        return BindingBuilder.bind(ttlQueue()).to(normalExchange()).with("ttlRouting");
    }

    /**
     * 死信配置
     */

    /**
     * @description: 声明死信交换机
            * @param: 持久的不会自动删除的交换机
            * @return:
            * @author lily via
            * @date: 2024/3/31 1:32
     */
    @Bean
    DirectExchange deadExchange() {
        return new DirectExchange(MqConstant.DEAD_LETTER_EXCHANGE, true, false);
    }

    /**
     * @description: 死信队列
            * @param:
            * @return:
            * @author lily via
            * @date: 2024/3/31 1:34
     */
    @Bean
    public Queue deadQueue() {
        return new Queue(MqConstant.DEAD_LETTER_QUEUE, true, false, false);
    }

    @Bean
    Binding deadRouteBinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(MqConstant.DEAD_LETTER_ROUTING_KEY);
    }

    /**
     * 转发到 死信队列，配置参数
     */
    private Map<String, Object> deadQueueArgs() {
        Map<String, Object> map = new HashMap<>();
        // 绑定该队列到死信交换机
        map.put("x-dead-letter-exchange", MqConstant.DEAD_LETTER_EXCHANGE);
        map.put("x-dead-letter-routing-key", MqConstant.DEAD_LETTER_ROUTING_KEY);
        return map;
    }
}
