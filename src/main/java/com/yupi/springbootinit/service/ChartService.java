package com.yupi.springbootinit.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.springbootinit.model.dto.chart.ChartQueryRequest;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.model.vo.ChartVO;
import com.yupi.springbootinit.model.vo.PostVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author 22825
* @description 针对表【chart(图表信息表)】的数据库操作Service
* @createDate 2024-03-12 10:34:35
*/
public interface ChartService extends IService<Chart> {

    ChartVO getChartVO(Chart chart, HttpServletRequest request);

    QueryWrapper<Chart> getQueryWrapper(ChartQueryRequest chartQueryRequest);

    Page<ChartVO> getChartVOPage(Page<Chart> chartPage, HttpServletRequest request);

//    Page<Chart> searchFromEs(ChartQueryRequest chartQueryRequest);
}
