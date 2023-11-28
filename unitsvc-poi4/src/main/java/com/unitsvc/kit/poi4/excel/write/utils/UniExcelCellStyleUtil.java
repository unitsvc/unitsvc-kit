package com.unitsvc.kit.poi4.excel.write.utils;

import cn.hutool.core.date.DateUtil;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellAttrLabelEnum;
import com.unitsvc.kit.poi4.excel.common.enums.UniExcelCellBoolFmtEnum;
import com.unitsvc.kit.poi4.excel.common.model.UniExcelHeaderReq;
import com.unitsvc.kit.poi4.excel.write.config.UniExcelStyleExportConfig;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.Date;
import java.util.List;

/**
 * 功能描述：动态单元格样式工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/13 17:42
 */
public class UniExcelCellStyleUtil {

    /**
     * 设置单元格居中样式
     *
     * @param style
     */
    public static void setCellCenterStyle(CellStyle style) {
        // 水平-居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 垂直-居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * 设置单元格右对齐样式
     *
     * @param style
     */
    public static void setCellRightStyle(CellStyle style) {
        // 水平-右对齐
        style.setAlignment(HorizontalAlignment.RIGHT);
        // 垂直-居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
    }

    /**
     * 设置标题单元格默认样式，宋体、22号、加粗
     *
     * @param font  字体
     * @param style 样式
     */
    public static void setCellTitleHeaderDefaultFont(Font font, CellStyle style) {
        // 字体名称
        font.setFontName("宋体");
        // 字体大小
        font.setFontHeightInPoints((short) 22);
        // 是否字体加粗
        font.setBold(true);
        // 设置字体样式
        style.setFont(font);
    }

    /**
     * 设置单元格字体样式
     *
     * @param font     字体
     * @param style    样式
     * @param fontName 字体名称，可选：宋体
     * @param fontSize 字体大小，可选：10、14、22
     * @param bold     是否加粗
     */
    public static void setCellFont(Font font, CellStyle style, String fontName, int fontSize, boolean bold) {
        // 字体名称
        font.setFontName(fontName);
        // 字体大小
        font.setFontHeightInPoints((short) fontSize);
        // 是否字体加粗
        font.setBold(bold);
        // 设置字体样式
        style.setFont(font);
    }

    /**
     * 设置单元格背景填充颜色-灰色
     *
     * @param style 样式
     */
    public static void setCellGreyStyle(CellStyle style) {
        // 设置灰色背景
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        // 设置填充模式
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }

    /**
     * 设置单元格细边框
     *
     * @param style 样式
     */
    public static void setCellBorderThinStyle(CellStyle style) {
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
    }

    /**
     * 设置单元格中等边框
     *
     * @param style 样式
     */
    public static void setCellBorderMediumStyle(CellStyle style) {
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderTop(BorderStyle.MEDIUM);
    }

    /**
     * 设置单元格为文本类型
     *
     * @param dataFormat 数据格式化
     * @param style      样式
     */
    public static void setCellTypeString(DataFormat dataFormat, CellStyle style) {
        // 文本
        style.setDataFormat(dataFormat.getFormat("@"));
    }

    /**
     * 设置单元格为浮点类型
     *
     * @param dataFormat                数据格式化
     * @param style                     单元格样式
     * @param dynamicHeaderDTO          单元格表头
     * @param uniExcelStyleExportConfig 表格导出配置
     */
    public static void setCellTypeFloat(DataFormat dataFormat, CellStyle style, UniExcelHeaderReq dynamicHeaderDTO, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        int decimalPointNum = uniExcelStyleExportConfig.getDecimalPointNum();
        // 获取展示标签
        List<UniExcelCellAttrLabelEnum> showLabels = dynamicHeaderDTO.getShowLabels();
        if (CollectionUtils.isNotEmpty(showLabels)) {
            // 过滤出数字类型标签
            UniExcelCellAttrLabelEnum numberLabel = showLabels.stream().filter(v -> v.name().startsWith("NUMBER")).findFirst().orElse(null);
            if (null != numberLabel) {
                if (numberLabel.equals(UniExcelCellAttrLabelEnum.NUMBER_TEXT)) {
                    // 设置单元格右对齐
                    UniExcelCellStyleUtil.setCellRightStyle(style);
                    // 设置为文本类型
                    style.setDataFormat(dataFormat.getFormat(numberLabel.getFormat()));
                    return;
                } else {
                    // 获取保留小数点位数
                    decimalPointNum = Integer.parseInt(numberLabel.getFormat());
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("0");
        for (int i = 0; i < decimalPointNum; i++) {
            if (i == 0) {
                sb.append(".0");
            } else {
                sb.append("0");
            }
        }
        // 备注：_加空格，导出为数值类型，否则会报错。`0.00_ `与`0.00`区别在于`0.00_ `展示时会多出空格，且空格可以多加，不影响数值类型。
        sb.append("_ ");
        // 整数
        style.setDataFormat(dataFormat.getFormat(sb.toString()));
        // --------------------------------- 备注 ------------------------------------
        // 1.`0.0#`含义：保留两位小数，第二位小数为0则不展示，如3.10 -> 3.1 | 3.125 -> 3.13
        // 2.`#,##0.0#`含义：千分位展示，保留2位小数，第2位小数为0则不展示。如：2000.12 -> 2,000.12 | 2000.10 -> 2,000.1
        // 3.`#,##` 含义：千分位展示。
        // 4.`0.00%` 含义：百分比展示。
        // --------------------------------------------------------------------------
    }

    /**
     * 设置单元格为整数类型
     *
     * @param dataFormat 数据格式化
     * @param style      样式
     */
    public static void setCellTypeInteger(DataFormat dataFormat, CellStyle style) {
        // 整数
        style.setDataFormat(dataFormat.getFormat("0_ "));
    }

    /**
     * 设置单元格为小数类型，保留两位小数
     *
     * @param dataFormat 数据格式化
     * @param style      样式
     */
    public static void setCellTypeDouble(DataFormat dataFormat, CellStyle style) {
        // 小数，保留两位小数点
        style.setDataFormat(dataFormat.getFormat("0.00_ "));
    }

    /**
     * 定义整数类型正则表达式
     */
    private static final String INTER_REGEX = "^[-\\+]?[\\d]*$";

    /**
     * 判断单元格是否是整数类型
     *
     * @param cellValue 单元格数值
     * @return 返回：true/整数、false/小数
     */
    public static boolean checkIsInteger(String cellValue) {
        return cellValue.matches(INTER_REGEX);
    }

    /**
     * 设置单元格为时间类型
     *
     * @param dataFormat 数据格式化
     * @param style      样式
     */
    public static void setCellTypeDate(DataFormat dataFormat, CellStyle style) {
        style.setDataFormat(dataFormat.getFormat("yyyy-MM-dd"));
    }

    /**
     * 设置单元格为自定义时间类型
     *
     * @param dataFormat                数据格式化
     * @param style                     单元格样式
     * @param dynamicHeaderDTO          单元格表头
     * @param uniExcelStyleExportConfig 表格导出配置
     */
    public static void setCellTypeCustomDate(DataFormat dataFormat, CellStyle style, UniExcelHeaderReq dynamicHeaderDTO, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        String formatStr = uniExcelStyleExportConfig.getDataFormatStr();
        // 获取展示标签
        List<UniExcelCellAttrLabelEnum> showLabels = dynamicHeaderDTO.getShowLabels();
        if (CollectionUtils.isNotEmpty(showLabels)) {
            // 过滤出时间类型标签
            UniExcelCellAttrLabelEnum dateLabel = showLabels.stream().filter(v -> v.name().startsWith("DATE")).findFirst().orElse(null);
            if (null != dateLabel) {
                // 获取时间格式化规则
                formatStr = dateLabel.getFormat();
            }
        }
        style.setDataFormat(dataFormat.getFormat(formatStr));
    }

    /**
     * 获取单元格为时间类型的值
     *
     * @param cellValue 单元格数值
     * @return 格式化后的单元格数值
     */
    public static Date formatCellTypeDateValue(String cellValue) {
        return DateUtil.parse(cellValue, "yyyy-MM-dd");
    }

    /**
     * 获取单元格为布尔类型的展示值，示例：Y|N、1|0、是|否、TRUE|FALSE
     *
     * @param cellValue                 单元格数值
     * @param dynamicHeaderDTO          单元格表头
     * @param uniExcelStyleExportConfig 表格导出配置
     * @return
     */
    public static String formatCellTypeBooleanValue(String cellValue, UniExcelHeaderReq dynamicHeaderDTO, UniExcelStyleExportConfig uniExcelStyleExportConfig) {
        // 获取全局导出配置
        UniExcelCellBoolFmtEnum boolFormatEnum = uniExcelStyleExportConfig.getBoolFormatEnum();
        String format = boolFormatEnum.getFormat();
        // 获取展示标签
        List<UniExcelCellAttrLabelEnum> showLabels = dynamicHeaderDTO.getShowLabels();
        if (CollectionUtils.isNotEmpty(showLabels)) {
            // 过滤出时间类型标签
            UniExcelCellAttrLabelEnum boolLabel = showLabels.stream().filter(v -> v.name().startsWith("BOOL")).findFirst().orElse(null);
            if (null != boolLabel) {
                // 获取布尔格式化规则
                format = boolLabel.getFormat();
            }
        }
        // 转换成小写，以防出现大写
        String lowerCase = cellValue.toLowerCase();
        String[] values = format.split("\\|");
        switch (lowerCase) {
            case "true":
                return values[0];
            case "false":
                return values[1];
            default:
        }
        return cellValue;
    }

}
