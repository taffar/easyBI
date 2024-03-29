package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.config.ThreadPoolExecuteConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lily via on 2024/3/24 16:46
 */
@RestController
@RequestMapping("/thread")
@Slf4j
@Profile({"dev", "local"})
public class ThreadController {

    @Resource
    ThreadPoolExecutor threadPoolExecute;

    @GetMapping("/add")
    public BaseResponse<String> addJob(@RequestParam("jobName") String jobName){
        long completedTaskCount;
        int poolSize;
        int jobSize;
        completedTaskCount = threadPoolExecute.getCompletedTaskCount();
        poolSize = threadPoolExecute.getPoolSize();
        jobSize = threadPoolExecute.getQueue().size();
        log.info("未进入异步方法前---当前线程名："+Thread.currentThread().getName()+",完成任务数："+completedTaskCount+",等待任务数："+jobSize+",当前线程数："+poolSize);
        CompletableFuture.runAsync(() -> {
            log.info("进入异步方法后---任务名"+jobName+"当前线程名："+Thread.currentThread().getName()+",完成任务数："+completedTaskCount+",等待任务数："+jobSize+",当前线程数："+poolSize);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, threadPoolExecute);
        return ResultUtils.success(jobName);
    }
}
