package com.unitsvc.kit.poi4.excel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：布尔类型格式化枚举
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/1/3 16:29
 **/
@Getter
@AllArgsConstructor
public enum UniExcelCellBoolFmtEnum {

    /**
     * 展示内容：Y|N
     */
    FMT_Y_N("Y|N"),

    /**
     * 展示内容：是|否
     */
    FMT_YES_NO("是|否"),

    /**
     * 展示内容：1|0
     */
    FMT_1_0("1|0"),

    /**
     * 展示内容：TRUE|FALSE
     */
    FMT_TRUE_FALSE("TRUE|FALSE");

    /**
     * 布尔类型格式化
     */
    private final String format;

}
