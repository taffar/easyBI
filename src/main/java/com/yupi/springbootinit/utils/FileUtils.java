package com.yupi.springbootinit.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by lily via on 2024/3/14 23:59
 * 处理excel等数据文件转换为csv格式的工具类
 */
public class FileUtils {

    // todo 数据正则获取
    public static String getFileString(MultipartFile multipartFile){
//        File file = null;
//        try {
//            file = ResourceUtils.getFile("classpath:网站数据.xlsx");
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /**
         * 拼接文件字符串
         */
        ThrowUtils.throwIf(CollUtil.isEmpty(list), ErrorCode.NOT_FOUND_ERROR, "文件读取失败");
        StringBuffer data = new StringBuffer();
        Iterator<Map<Integer, String>> it = list.iterator();
        while (it.hasNext()){
            /**
             * 过滤null行
             */
            Map<Integer, String> row = it.next();
            if (CollUtil.isEmpty(row)) continue;
            for (String value : row.values()) {
                /**
                 * 过滤null单元格
                 */
                if (StringUtils.isEmpty(value)) continue;
                data.append(value);
                data.append(',');
            }
            data.append("\\n");
        }
        return data.toString();
    }


}
