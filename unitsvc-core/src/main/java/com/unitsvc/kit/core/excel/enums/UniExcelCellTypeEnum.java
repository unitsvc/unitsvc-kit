package com.unitsvc.kit.core.excel.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：单元格类型枚举值
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 12:11
 **/
@Getter
@AllArgsConstructor
public enum UniExcelCellTypeEnum {

    /**
     * 文本类型
     */
    STRING("STRING", "文本"),

    /**
     * 数值类型
     */
    NUMBER("NUMBER", "数值"),

    /**
     * 日期类型
     */
    DATE("DATE", "日期"),

    /**
     * 布尔类型
     */
    BOOLEAN("BOOLEAN", "布尔"),

    /**
     * 常规类型
     */
    NORMAL("NORMAL", "常规"),

    /**
     * 公式类型
     */
    FORMULA("FORMULA", "公式"),

    /**
     * 错误类型
     */
    ERROR("ERROR", "错误"),

    ;

    /**
     * 英文名称
     */
    private final String nameEn;

    /**
     * 中文名称
     */
    private final String nameCn;

}
