package com.unitsvc.kit.poi4.excel.read.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Date;

/**
 * 功能描述：单元格数据类型处理工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/11/2 12:48
 **/
public class UniExcelCellDateUtil {

    /**
     * 异常时间格式，2022-11-01 12:01:00:588
     */
    private static final int ERROR_TIME_LENGTH = 4;

    /**
     * 13位，毫秒时间戳，1667367252000
     */
    private static final int TIMESTAMP_13_LENGTH = 13;

    /**
     * 10位，秒时间戳，1667367252
     */
    private static final int TIMESTAMP_10_LENGTH = 10;

    /**
     * 非规则时间格式，202211011212
     */
    private static final int SPEC_TIME_LENGTH = 12;

    /**
     * 时间类型转换，包含处理异常时间，例如：2022-11-01 12:01:00:588，此数据为数仓异常数据
     *
     * @param time 时间字符串
     * @return
     */
    public static Date handleTime(String time) {
        try {
            if (NumberUtil.isNumber(time)) {
                // 解析时间戳及特殊类型
                // 13位时间戳，1667367252755
                if (time.length() == TIMESTAMP_13_LENGTH) {
                    return DateUtil.date(Long.parseLong(time));
                }
                // 10位时间戳，1667367252
                if (time.length() == TIMESTAMP_10_LENGTH) {
                    return DateUtil.date(Long.parseLong(time) * 1000);
                }
                // 特殊时间：202211011212
                if (time.length() == SPEC_TIME_LENGTH) {
                    return DateUtil.parse(String.format("%s00", time));
                }
            } else {
                // 解析正常时间类型
                return DateUtil.parse(time);
            }
        } catch (Exception e) {
            try {
                String[] timeArr = time.split(":");
                if (timeArr.length == ERROR_TIME_LENGTH) {
                    // 错误时间格式
                    String realTime = StrUtil.removeSuffix(time, String.format(":%s", timeArr[3]));
                    return DateUtil.parse(realTime);
                }
                // 特殊时间格式
                return DateUtil.parse(time, "yyyy-MM");
            } catch (Exception ex) {
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Date d1 = handleTime("1667367252");
        Date d2 = handleTime("1667367252000");
        Date d3 = handleTime("202211011212");
        Date d4 = handleTime("2022-11-01 12:01:00:588");
        Date d5 = handleTime("2023-02-28");
        Date d6 = handleTime("2023-02");
        Date d7 = handleTime("2023-021");
        System.out.println("d1 = " + d1);
        System.out.println("d2 = " + d2);
        System.out.println("d3 = " + d3);
        System.out.println("d4 = " + d4);
        System.out.println("d5 = " + d5);
        System.out.println("d6 = " + d6);
        System.out.println("d7 = " + d7);
    }

}
