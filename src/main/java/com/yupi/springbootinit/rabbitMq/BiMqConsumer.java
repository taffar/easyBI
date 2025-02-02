package com.yupi.springbootinit.rabbitMq;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.ChartConstant;
import com.yupi.springbootinit.constant.MqConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.manager.AIManager;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * Created by lily via on 2024/3/29 10:23
 */
@Component
@Slf4j
// TODO 实现死信消息进入死信队列再处理
public class BiMqConsumer {
    private int cnt = 1;

    @Resource
    private ChartService ChartService;

    @Resource
    private AIManager aiManager;


    /**
     * 定义了接收消息后的处理流程
     * 监听器指定了要监听的队列，ack模式为手动确认
     *
     * @param channel     用于处理返回消息接收状态
     * @param message     图表存入数据库后主键id
     * @param deliveryTag 消息体唯一标签
     */
    @RabbitListener(queues = {MqConstant.BI_WORK_QUEUE}, concurrency = "4")
    public void receiveMessage(Channel channel, String message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        // todo 校验message
        long chartId = Long.parseLong(message);
        log.info("receive a message {}", chartId);

        Chart chartRunning = new Chart();
        // 任务处理中，修改状态
        chartRunning.setId(chartId);
        chartRunning.setChartStatus(ChartConstant.RUNNING);
        boolean running = ChartService.updateById(chartRunning);
        if (!running) {
            ChartService.handleGenChartError(chartId, "更新图表running状态失败");
        }
        // 获得用户输入拼接后的ai对话
        Chart savedChart = ChartService.getById(chartId);
        String csv = savedChart.getChartData();
        String goal = savedChart.getGoal();
        String chartType = savedChart.getChartType();
        String aiQuestion = ChartService.getAiQuestion(csv, goal, chartType);
        // 调用ai接口
        String rawData = aiManager.sendMesToAIUseXingHuo(aiQuestion);
        // 处理数据 TODO 正则清洗数据
        String[] splits = StringUtils.split(rawData, "【【【【【");
        // 生成数据校验
        if (splits.length < 2 || StringUtils.isBlank(splits[0]) || StringUtils.isBlank(splits[1])) {
            // todo
            log.error("图表: {},ai生成消息失败,第x次尝试重试生成", message);
            throw new BusinessException(ErrorCode.AI_GEN_ERROR, "ai生成消息错误");
        }
        String code = splits[0].trim();
        String analyse = splits[1].trim();

        // 生成图表代码存储
        Chart Chart = new Chart();
        Chart.setGenChart(code);
        Chart.setGenResult(analyse);
        // 生成成功更改状态
        Chart.setChartStatus(ChartConstant.SUCCEED);
        boolean succeed = ChartService.save(Chart);
        if (!succeed) {
           ChartService.handleGenChartError(chartId, "更新图表succeed状态失败");
        }
        try {
            channel.basicAck(deliveryTag, false);
            log.info("message {}: ack succeed", message);
        } catch (IOException e) {
            log.error("message {}: ack failed", message);
            throw new RuntimeException(e);
        }
    }
}
