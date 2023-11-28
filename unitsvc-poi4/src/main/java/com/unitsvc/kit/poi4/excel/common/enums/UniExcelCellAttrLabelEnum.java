package com.unitsvc.kit.poi4.excel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：定义表格的表头属性标签常量
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/1/4 9:55
 **/
@Getter
@AllArgsConstructor
public enum UniExcelCellAttrLabelEnum {

    // -------------------------------------------------- 单元格属性标签 --------------------------------------------------

    /**
     * 强制单元格为文本类型
     * <p>
     * 例如：01010、2023-01-04，若为常规类型则当前单元格数据类型修改后容易改变成数值或者其它格式时间类型
     */
    ATTR_TEXT("@", "强制设置常规类型单元格为文本类型，而不是常规类型，仅对表头为字符串所在列生效"),

    /**
     * 生成布尔值下拉选项
     * <p>
     * 说明：与BOOL开头标签配合使用才生效，且优先级较高
     */
    OPTIONS_BOOL("\\|", "生成布尔值下拉框选项"),

    // -------------------------------------------------- 单元格宽度标签 --------------------------------------------------

    /**
     * 单元格宽度设置为10
     * <p>
     * 说明：适用于姓名、年龄等
     */
    WIDTH_10("10", "单元格宽度为10，适用于姓名、年龄等"),

    /**
     * 单元格宽度设置为15
     * <p>
     * 说明：适用于年月日时间、布尔等
     */
    WIDTH_15("15", "单元格宽度为15，适用于年月日时间、布尔等"),

    /**
     * 单元格宽度设置为20
     * <p>
     * 说明：适用于年月日时分秒时间、雪花算法主键等
     */
    WIDTH_20("20", "单元格宽度为20，适用于年月日时分秒时间、雪花算法主键等"),

    // -------------------------------------------------- 数字类型格式化标签 --------------------------------------------------

    /**
     * 保留整数
     */
    NUMBER_0("0", "只保留整数部分"),

    /**
     * 保留一位小数
     */
    NUMBER_1("1", "小数点后，保留一位小数"),

    /**
     * 保留两位小数
     */
    NUMBER_2("2", "小数点后，保留两位小数"),

    /**
     * 保留三位小数
     */
    NUMBER_3("3", "小数点后，保留三位小数"),

    /**
     * 保留四位小数
     */
    NUMBER_4("4", "小数点后，保留四位小数"),

    /**
     * 保留五位小数
     */
    NUMBER_5("5", "小数点后，保留五位小数"),

    /**
     * 保留六位小数
     */
    NUMBER_6("6", "小数点后，保留六位小数"),

    /**
     * 保留七位小数
     */
    NUMBER_7("7", "小数点后，保留七位小数"),

    /**
     * 保留八位小数
     */
    NUMBER_8("8", "小数点后，保留八位小数"),

    /**
     * 保留九位小数
     */
    NUMBER_9("9", "小数点后，保留九位小数"),

    /**
     * 保留十位小数
     */
    NUMBER_10("10", "小数点后，保留十位小数"),

    /**
     * 保留十一位小数
     */
    NUMBER_11("11", "小数点后，保留十一位小数"),

    /**
     * 保留十二位小数
     */
    NUMBER_12("12", "小数点后，保留十二位小数"),

    /**
     * 保留十三位小数
     */
    NUMBER_13("13", "小数点后，保留十三位小数"),

    /**
     * 保留十四位小数
     */
    NUMBER_14("14", "小数点后，保留十四位小数"),

    /**
     * 保留十五位小数
     * <p>
     * 备注：表格最多支持15位小数
     */
    NUMBER_15("15", "小数点后，保留十五位小数"),

    /**
     * 以文本格式展示实际数值
     * <p>
     * 备注：表格最多展示9位小数
     */
    NUMBER_TEXT("@", "以文本格式展示实际数值，但是最多展示九位小数"),

    // ------------------------------------------------------ 时间类型格式化标签 ----------------------------------------------

    /**
     * 展示年月，示例：2022-11
     */
    DATE_YM_0("yyyy-MM", "2022-11"),

    /**
     * 展示年月，示例：2022/11
     */
    DATE_YM_1("yyyy/MM", "2022/11"),

    /**
     * 展示年月日，示例：2022-11-25
     */
    DATE_YMD_0("yyyy-MM-dd", "2022-11-25"),

    /**
     * 展示年月日，示例：2022/11/25
     */
    DATE_YMD_1("yyyy/MM/dd", "2022/11/25"),

    /**
     * 展示年月日，示例：2023/1/4
     */
    DATE_YMD_2("yyyy/M/d", "2023/1/4"),

    /**
     * 展示年月日时分，示例：2022-11-25 14:00
     */
    DATE_YMD_HM_0("yyyy-MM-dd HH:mm", "2022-11-25 14:00"),

    /**
     * 展示年月日时分，示例：2022/11/25 14:00
     */
    DATE_YMD_HM_1("yyyy/MM/dd HH:mm", "2022/11/25 14:00"),

    /**
     * 展示年月日时分秒，示例：2022-11-25 14:00:00
     */
    DATE_YMD_HMS_0("yyyy-MM-dd HH:mm:ss", "2022-11-25 14:00:00"),

    /**
     * 展示年月日时分秒，示例：2022/11/25 14:00:00
     */
    DATE_YMD_HMS_1("yyyy/MM/dd HH:mm:ss", "2022/11/25 14:00:00"),

    // ------------------------------------------------------ 布尔类型格式化标签 ----------------------------------------------

    /**
     * 展示是否
     */
    BOOL_YN_CN("是|否", "是|否"),

    /**
     * 展示YN
     */
    BOOL_YN_EN("Y|N", "Y|N"),

    /**
     * 展示TRUE|FALSE
     */
    BOOL_TF_UPPER("TRUE|FALSE", "TRUE|FALSE"),

    /**
     * 展示true|false
     * <p>
     * 说明：不建议使用，因为触发修改时表格会将该值转换成大写
     */
    @Deprecated
    BOOL_TF_LOWER("true|false", "true|false"),

    /**
     * 展示1|0
     */
    BOOL_10_NUM("1|0", "1|0");

    /**
     * 格式化
     */
    private final String format;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据名称获取标签枚举
     *
     * @param name 名称
     * @return
     */
    public static UniExcelCellAttrLabelEnum getLabelByName(String name) {
        for (UniExcelCellAttrLabelEnum label : UniExcelCellAttrLabelEnum.values()) {
            if (label.name().equals(name)) {
                return label;
            }
        }
        return null;
    }

    /**
     * 获取小数位数标签枚举
     *
     * @param decimal 保留小数点位数
     * @return
     */
    public static UniExcelCellAttrLabelEnum getNumberLabel(Integer decimal) {
        if (null != decimal) {
            for (UniExcelCellAttrLabelEnum label : UniExcelCellAttrLabelEnum.values()) {
                if (label.name().equals(String.format("NUMBER_%s", decimal))) {
                    return label;
                }
            }
        }
        return null;
    }

}
