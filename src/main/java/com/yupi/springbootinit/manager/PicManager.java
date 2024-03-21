package com.yupi.springbootinit.manager;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 用于处理图片上传保存等相关操作
 * Created by lily via on 2024/3/21 18:04
 */
public class PicManager {

    public void inputStringToByte(MultipartFile pic){
        InputStream inputStream = null; // 获取输入流，可以是FileInputStream、ByteArrayInputStream等
        try {
            inputStream = pic.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;
        while (true) {
            try {
                if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            outputStream.write(buffer, 0, bytesRead);
        }

        byte[] allBytes = outputStream.toByteArray();

// 现在allBytes中包含了输入流中的所有字节数据

    }

}
