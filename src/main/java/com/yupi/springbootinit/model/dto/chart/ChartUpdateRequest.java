package com.yupi.springbootinit.model.dto.chart;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 更新请求
 */
@Data
public class ChartUpdateRequest implements Serializable {

    /**
     * 分析目标
     */
    private String goal;

    /**
     * 图表名称
     */
    private String name;

    /**
     * 目标图表数据
     */
    private String chartData;

    /**
     * 生成图表的类型
     */
    private String chartType;

    private static final long serialVersionUID = 1L;
}