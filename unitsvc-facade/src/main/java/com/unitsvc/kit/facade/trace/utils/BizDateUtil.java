package com.unitsvc.kit.facade.trace.utils;

import cn.hutool.core.util.StrUtil;
import com.jpardus.spider.sccs.Log;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * 功能描述：时间工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/1/9 14:03
 **/
public class BizDateUtil {

    /**
     * 自定义调整当前年月
     *
     * @param monthAmount 调整月份，0->当前月份、-1->上个月份、1->下个月份
     * @return
     */
    public static YearMonthInfo adjustNowYearMouthByCustom(Integer monthAmount) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, monthAmount);
        // 获取当前年份
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月份，因为月份是从0开始的，所以要加1
        int month = (calendar.get(Calendar.MONTH)) + 1;
        return YearMonthInfo.builder()
                .yearNum(year)
                .monthNum(month)
                .yearMonth(cn.hutool.core.date.DateUtil.format(calendar.getTime(), "yyyy-MM"))
                .build();
    }

    /**
     * 自定义调整当前年月
     *
     * @param yearMonth   待调整的年月，格式：yyyy-MM、示例：2023-01
     * @param monthAmount 调整月份，0->当前月份、-1->上个月份、1->下个月份
     * @return
     */
    public static YearMonthInfo adjustNowYearMouthByCustom(String yearMonth, Integer monthAmount) {
        Calendar calendar = Calendar.getInstance();
        Date date = cn.hutool.core.date.DateUtil.parse(yearMonth, "yyyy-MM");
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthAmount);
        // 获取当前年份
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月份，因为月份是从0开始的，所以要加1
        int month = (calendar.get(Calendar.MONTH)) + 1;
        return YearMonthInfo.builder()
                .yearNum(year)
                .monthNum(month)
                .yearMonth(cn.hutool.core.date.DateUtil.format(calendar.getTime(), "yyyy-MM"))
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
    public static YearMonthInfo adjustNowYearMouthByCustom(Integer yearNum, Integer monthNum, Integer monthAmount) {
        Calendar calendar = Calendar.getInstance();
        String yearMonth = String.format("%s-%s", yearNum, monthNum <= 9 ? "0" + monthNum : monthNum);
        Date date = cn.hutool.core.date.DateUtil.parse(yearMonth, "yyyy-MM");
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, monthAmount);
        // 获取当前年份
        int year = calendar.get(Calendar.YEAR);
        // 获取当前月份，因为月份是从0开始的，所以要加1
        int month = (calendar.get(Calendar.MONTH)) + 1;
        return YearMonthInfo.builder()
                .yearNum(year)
                .monthNum(month)
                .yearMonth(cn.hutool.core.date.DateUtil.format(calendar.getTime(), "yyyy-MM"))
                .build();
    }

    /**
     * 时间长度
     */
    private static final Integer TIME_LENGTH_13 = 13;

    /**
     * 时间长度
     */
    private static final Integer TIME_LENGTH_10 = 10;

    /**
     * 格式化时间，转13位时间戳
     *
     * @param strTime 格式化的时间，支持常见格式：yyyy-MM-dd HH:mm、yyyy-MM-dd、yyyy-MM、13位时间戳、10位时间戳
     * @return
     */
    public static Long toTimestamp(String strTime) {
        try {
            if (StringUtils.isNotEmpty(strTime)) {
                // 常规时间格式
                return cn.hutool.core.date.DateUtil.parse(strTime).getTime();
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
            } catch (Exception ex) {
                try {
                    // 特殊时间格式
                    return cn.hutool.core.date.DateUtil.parse(strTime, "yyyy-MM").getTime();
                } catch (Exception exc) {
                    Log.error(String.format("【无效时间格式：%s】", ex.getMessage()), e);
                }
            }
        }
        return null;
    }

    /**
     * 时间戳转格式化时间
     *
     * @param timestamp 10位时间戳，13位时间戳
     * @param format    格式化方式：yyyy-MM-dd、yyyy-MM、yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String toFmtStrTime(Long timestamp, String format) {
        if (null != timestamp) {
            int length = String.valueOf(timestamp).length();
            if (length == TIME_LENGTH_10) {
                return cn.hutool.core.date.DateUtil.format(new Date(timestamp * 1000L), format);
            }
            if (length == TIME_LENGTH_13) {
                return cn.hutool.core.date.DateUtil.format(new Date(timestamp), format);
            }
        }
        return "";
    }

    public static void main(String[] args) {
        YearMonthInfo yearMonthInfo = adjustNowYearMouthByCustom(2023, 1, -1);
        System.out.println("yearMonthInfo = " + yearMonthInfo);
    }

    @Data
    @Builder
    public static class YearMonthInfo implements Serializable {

        private static final long serialVersionUID = 7075063531994939301L;

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
