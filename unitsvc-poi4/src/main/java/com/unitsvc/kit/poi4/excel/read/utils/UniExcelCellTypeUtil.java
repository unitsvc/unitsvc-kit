package com.unitsvc.kit.poi4.excel.read.utils;

import cn.hutool.core.date.DateUtil;
import com.unitsvc.kit.poi4.excel.common.exception.UniV4ExcelException;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * 功能描述：excel导入后数据类型转换工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/17 19:01
 */
@SuppressWarnings("all")
public class UniExcelCellTypeUtil {

    /**
     * excel的货币类型逗号
     */
    private static final String EXCEL_NUM_COMMA = ",";

    /**
     * excel的科学计数法的指数E
     */
    private static final String EXCEL_NUM_E = "E";

    /**
     * 转换数值类型时间为标准时间字符串，格式：yyyy-MM-dd
     *
     * @param cellValue 单元格数值
     * @return
     */
    public static String toDateStr(String cellValue) {
        if (StringUtils.isEmpty(cellValue)) {
            return null;
        }
        try {
            // 转换数值时间，为java时间
            Date javaDate = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(Double.parseDouble(cellValue));
            return DateUtil.format(javaDate, "yyyy-MM-dd HH:mm:ss");
        } catch (NumberFormatException e) {
            // 默认格式化
            return DateUtil.format(DateUtil.parse(cellValue), "yyyy-MM-dd HH:mm:ss");
        }
    }

    /**
     * 单元格数据转换为小数字符串
     *
     * @param cellValue 单元格数值
     * @return
     */
    public static String toDoubleStr(String cellValue) {
        String numberStr = cellValue;
        if (numberStr.contains(EXCEL_NUM_COMMA)) {
            // 货币格式
            numberStr = numberStr.replace(",", "");
        }
        if (numberStr.contains(EXCEL_NUM_E)) {
            // 科学计算法
            return new BigDecimal(cellValue).toPlainString();
        }
        // 校验字段有效性，若非数字则异常
        return new BigDecimal(numberStr).toPlainString();
    }

    /**
     * 单元格数据转换为整数字符串
     *
     * @param cellValue 单元格数值
     * @return
     */
    public static String toIntegerStr(String cellValue) {
        String numberStr = cellValue;
        if (numberStr.contains(EXCEL_NUM_COMMA)) {
            // 货币格式
            numberStr = numberStr.replace(",", "");
        }
        if (numberStr.contains(EXCEL_NUM_E)) {
            // 科学计算法
            return new BigDecimal(cellValue).toPlainString();
        }
        // 校验字段有效性，若非数字则异常
        return new BigDecimal(numberStr).setScale(0, RoundingMode.HALF_UP).toPlainString();
    }

    /**
     * 单元格数据转换为布尔字符串
     *
     * @param cellValue 单元格数值
     * @return
     */
    public static String toBooleanStr(String cellValue) {
        if (StringUtils.isNotEmpty(cellValue)) {
            switch (cellValue.trim().toUpperCase()) {
                case "是":
                case "1":
                case "Y":
                case "TRUE":
                    return "true";
                case "否":
                case "0":
                case "N":
                case "FALSE":
                    return "false";
                default:
                    // pass
                    throw new UniV4ExcelException("单元格内容填写有误，有效值：1/0、是/否、Y/N、TRUE/FALSE");
            }
        }
        return cellValue;
    }
}
