package com.yupi.springbootinit.model.vo;

import cn.hutool.json.JSONUtil;
import com.yupi.springbootinit.model.entity.Chart;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 帖子视图
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
@Data
public class ChartVO implements Serializable {

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

    /**
     * 生成的图表
     */
    private String genChart;

    /**
     * 创建用户 id
     */
    private UserVO user;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 包装类转对象
     *
     * @param ChartVO
     * @return
     */
    public static Chart voToObj(ChartVO ChartVO) {
        if (ChartVO == null) {
            return null;
        }
        Chart Chart = new Chart();
        BeanUtils.copyProperties(ChartVO, Chart);
        return Chart;
    }

    /**
     * 对象转包装类
     *
     * @param Chart
     * @return
     */
    public static ChartVO objToVo(Chart Chart) {
        if (Chart == null) {
            return null;
        }
        ChartVO ChartVO = new ChartVO();
        BeanUtils.copyProperties(Chart, ChartVO);
        return ChartVO;
    }
}
