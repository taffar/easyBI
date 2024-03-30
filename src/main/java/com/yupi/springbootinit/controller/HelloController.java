package com.yupi.springbootinit.controller;

import cn.hutool.json.JSONUtil;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wxd
 * @version V1.0
 * @description HelloController
 * @date 2022/6/13 13:59
 **/
@RestController
@RequestMapping("/mq")
public class HelloController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 正常消息队列，队列最大长度5
     */
    @GetMapping("/normalQueue")
    public String normalQueue() {
        Map<String, Object> map = new HashMap<>(2);
        map.put("reTryCount", 0);
        map.put("data", System.currentTimeMillis() + ", 正常队列消息，最大长度 5");

        CorrelationData correlationData = new CorrelationData("id_" + System.currentTimeMillis());
        rabbitTemplate.convertAndSend("normalExchange", "normalRouting", map,  correlationData);
        return JSONUtil.toJsonStr(map);
    }

    /**
     * 消息 TTL, time to live
     */
    @GetMapping("/ttlToDead")
    public String ttlToDead() {

        Map<String, Object> map = new HashMap<>();
//        map.put("messageId", String.valueOf(UUID.randomUUID()));
        map.put("data", System.currentTimeMillis() + ", ttl队列消息");
        CorrelationData correlationData = new CorrelationData("id_" + System.currentTimeMillis());

        rabbitTemplate.convertAndSend("normalExchange", "ttlRouting", map, correlationData);
        return JSONUtil.toJsonStr(map);
    }

}
