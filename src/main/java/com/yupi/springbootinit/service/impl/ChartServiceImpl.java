package com.yupi.springbootinit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.constant.ChartConstant;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.mapper.ChartMapper;
import com.yupi.springbootinit.model.dto.chart.ChartQueryRequest;
import com.yupi.springbootinit.model.entity.*;
import com.yupi.springbootinit.model.vo.ChartVO;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.service.UserService;
import com.yupi.springbootinit.utils.FileUtils;
import com.yupi.springbootinit.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 图表服务实现
 */
@Service
@Slf4j
public class ChartServiceImpl extends ServiceImpl<ChartMapper, Chart> implements ChartService {

    @Resource
    private UserService userService;


    /**
     * 获取查询包装类
     *
     * @param ChartQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest ChartQueryRequest) {

        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        if (ChartQueryRequest == null) {
            return queryWrapper;
        }
        String goal = ChartQueryRequest.getGoal();
        String name = ChartQueryRequest.getName();
        String chartData = ChartQueryRequest.getChartData();
        String chartType = ChartQueryRequest.getChartType();
        Long userId = ChartQueryRequest.getUserId();
        String sortField = ChartQueryRequest.getSortField();
        String sortOrder = ChartQueryRequest.getSortOrder();
        // 拼接查询条件
        if (!StringUtils.isAnyBlank(name, goal)) {
            queryWrapper.and(qw -> qw.like("name", name).or().like("goal", goal));
        }
        queryWrapper.like(StringUtils.isNotBlank(chartType), "chartType", chartType);
        queryWrapper.like(StringUtils.isNotBlank(chartData), "chartData", chartData);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request) {
        return null;
    }


    @Override
    public ChartVO getChartVO(Chart chart, HttpServletRequest request) {
        return null;
    }

    @Override
    public void handleGenChartError(long chartId, String message) {
        log.error("图表更新状态失败");
        Chart updateChart = new Chart();
        updateChart.setId(chartId);
        updateChart.setChartStatus(ChartConstant.FAILED);
        updateChart.setExecutorMessage(message);
        boolean b = updateById(updateChart);
        if (!b) {
            log.error("更新图表失败状态失败");
            throw new BusinessException(ErrorCode.AI_GEN_ERROR, message);
        }
    }

    /**
     * 构造ai查询
     *
     * @return ai查询的问题字符串
     */
    @Override
    public String getAiQuestion(String csv, String goal, String chartType) {
        StringBuilder builder = new StringBuilder();
        builder.append("你是一名优秀的数据分析师，根据分析目标:" + goal + ",以及以下数据帮我生成一个" + chartType + "图表。");
        builder.append(csv);
        return builder.toString();
    }
}




