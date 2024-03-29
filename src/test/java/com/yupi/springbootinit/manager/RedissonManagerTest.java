package com.yupi.springbootinit.manager;

import ch.qos.logback.core.joran.conditional.IfAction;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.Test;
import org.redisson.api.RRateLimiter;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by lily via on 2024/3/23 12:02
 */
@SpringBootTest
class RedissonManagerTest {

    @Resource
    RedissonManager redissonManager;

    @Test
    public void rateLimiterTest(){

        for (int i = 0; i < 8; i++) {
            redissonManager.doReteLimiter("lock1");
            System.out.println(i+","+Thread.currentThread().getName());
        }
    }
}