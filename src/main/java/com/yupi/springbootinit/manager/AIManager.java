package com.yupi.springbootinit.manager;

import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

/**
 * Created by lily via on 2024/3/20 20:45
 */
@Service
public class AIManager {

    String accessKey= "zjs1w96jsvarrupi5f8n0dfrhxt6bqr";
    String secretKey=  "jkf3qjqau1vpwzfrm37xmzli7kt3xch8";

    Long modelId= 1770442434330185730L;
    YuCongMingClient client = new YuCongMingClient(accessKey, secretKey);

    public void doChat(String message){
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = client.doChat(devChatRequest);
        System.out.println(response.getData());
    }
}
