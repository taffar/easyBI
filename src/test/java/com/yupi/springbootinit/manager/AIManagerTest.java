package com.yupi.springbootinit.manager;

import cn.hutool.http.HttpUtil;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by lily via on 2024/3/21 0:18
 */
@SpringBootTest
class AIManagerTest {

    String originData = "你是一名优秀的数据分析师，根据分析目标:" + "网站访问情况" + ",以及以下数据帮我生成一个" + "折线图" + "类型的图表。";

    @Test
    public void chatTest(){
        // 调用ai接口
        String accessKey = "zjs1w96jsvarrupi5f8n0dfrhxt6bqr";
        String secretKey = "jkf3qjqau1vpwzfrm37xmzli7kt3xch8";
        YuCongMingClient yuCongMingClient = new YuCongMingClient(accessKey, secretKey);
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(1770442517520011266L);
        devChatRequest.setMessage(this.originData);
        com.yupi.yucongming.dev.common.BaseResponse<DevChatResponse> devChatResponseBaseResponse = yuCongMingClient.doChat(devChatRequest);
        System.out.println(devChatResponseBaseResponse);
        DevChatResponse data = devChatResponseBaseResponse.getData();
        if (data != null) {
            String content = data.getContent();
            System.out.println(content);
        }
    }

    public void xingHuoChatTest(){
        // 发送GET请求
        String url = "wss://spark-api.xf-yun.com/v3.5/chat";
        String response = HttpUtil.get(url);
        System.out.println("GET请求响应：" + response);

        // 发送POST请求
        String postUrl = "https://api.example.com/submit";
        String postData = "key=value&param2=value2";
        String postResponse = HttpUtil.post(postUrl, postData);
        System.out.println("POST请求响应：" + postResponse);

    }

}