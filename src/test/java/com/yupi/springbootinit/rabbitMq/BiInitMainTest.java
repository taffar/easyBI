package com.yupi.springbootinit.rabbitMq;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by lily via on 2024/3/29 16:24
 */
//@SpringBootTest
class BiInitMainTest {


    @Resource
    BiMqProducer producer;

//    @Test
    public void messageTest(){
            producer.sendMessage("message");
    }
}