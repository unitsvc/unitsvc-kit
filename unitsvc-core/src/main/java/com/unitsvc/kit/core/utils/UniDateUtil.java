package com.unitsvc.kit.core.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能描述：通用时间工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/22 12:53
 */
public class UniDateUtil {

    /**
     * 时间长度
     */
    private static final Integer TIME_LENGTH_13 = 13;

    /**
     * 时间长度
     */
    private static final Integer TIME_LENGTH_10 = 10;

    /**
     * 时间转13位时间戳
     *
     * @param strTime 字符串时间
     *                <p>
     *                支持常见格式：
     *                <p>
     *                yyyy-MM-dd HH:mm、yyyy-MM-dd、yyyy-MM
     *                <p>
     *                13位时间戳、10位时间戳
     * @return
     */
    public static Long toTimestamp(String strTime) {
        try {
            if (StringUtils.isNotEmpty(strTime)) {
                // 常规时间格式
                return DateUtil.parse(strTime).getTime();
            }
        } catch (Exception e) {
            try {
                if (StrUtil.isNumeric(strTime)) {
                    // 时间戳格式
                    if (strTime.length() == TIME_LENGTH_10) {
                        return Long.parseLong(strTime) * 1000;
                    }
                    if (strTime.length() == TIME_LENGTH_13) {
                        return Long.parseLong(strTime);
                    }
                }
            } catch (Exception e1) {
                try {
                    // 特殊时间格式
                    return DateUtil.parse(strTime, "yyyy-MM").getTime();
                } catch (Exception e2) {
                    throw new RuntimeException(String.format("时间格式无效【%s】", e2.getMessage()), e);
                }
            }
        }
        return null;
    }

    /**
     * 时间戳转格式化时间
     *
     * @param timestamp 10位时间戳，13位时间戳
     * @param format    格式化方式
     *                  <p>
     *                  yyyy-MM-dd、yyyy-MM、yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String toFmtStrTime(Long timestamp, String format) {
        if (null != timestamp) {
            int length = String.valueOf(timestamp).length();
            if (length == TIME_LENGTH_10) {
                return DateUtil.format(new Date(timestamp * 1000L), format);
            }
            if (length == TIME_LENGTH_13) {
                return DateUtil.format(new Date(timestamp), format);
            }
        }
        return "";
    }

    /**
     * 自定义调整当前年月
     *
     * @param monthAmount 调整月份，0->当前月份、-1->上个月份、1->下个月份
     * @return
     */
    public static YearMonthInner adjustNowYearMouthByCustom(Integer monthAmount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthAmount);
        // 获取当前年份
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月份，因为月份是从0开始的，所以要加1
        int month = (calendar.get(Calendar.MONTH)) + 1;
        return YearMonthInner.builder()
                .yearNum(year)
                .monthNum(month)
                .yearMonth(DateUtil.format(calendar.getTime(), "yyyy-MM"))
                .build();
    }

    /**
     * 自定义调整当前年月
     *
     * @param yearMonth   待调整的年月，格式：yyyy-MM、示例：2023-01
     * @param monthAmount 调整月份，0->当前月份、-1->上个月份、1->下个月份
     * @return
     */
    public static YearMonthInner adjustNowYearMouthByCustom(String yearMonth, Integer monthAmount) {
        Calendar calendar = Calendar.getInstance();
        Date date = DateUtil.parse(yearMonth, "yyyy-MM");
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthAmount);
        // 获取当前年份
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月份，因为月份是从0开始的，所以要加1
        int month = (calendar.get(Calendar.MONTH)) + 1;
        return YearMonthInner.builder()
                .yearNum(year)
                .monthNum(month)
                .yearMonth(DateUtil.format(calendar.getTime(), "yyyy-MM"))
                .build();
    }

    /**
     * 自定义调整当前年月
     *
     * @param yearNum     待调整年份，示例：2023
     * @param monthNum    待调整月份，示例：1
     * @param monthAmount 调整月份，0->当前月份、-1->上个月份、1->下个月份
     * @return
     */
    public static YearMonthInner adjustNowYearMouthByCustom(Integer yearNum, Integer monthNum, Integer monthAmount) {
        String yearMonth = String.format("%s-%s", yearNum, monthNum <= 9 ? "0" + monthNum : monthNum);
        return adjustNowYearMouthByCustom(yearMonth, monthAmount);
    }

    /**
     * 年月模型
     */
    @Data
    @Builder
    public static class YearMonthInner implements Serializable {

        private static final long serialVersionUID = -69499288695747952L;

        /**
         * 年份
         */
        private int yearNum;

        /**
         * 月份
         */
        private int monthNum;

        /**
         * 格式化年月
         * <p>
         * 备注：格式：yyyy-MM，示例：2023-01
         */
        private String yearMonth;
    }

}
