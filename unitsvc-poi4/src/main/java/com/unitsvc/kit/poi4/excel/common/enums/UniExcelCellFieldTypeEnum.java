package com.unitsvc.kit.poi4.excel.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author :
 * @version : v1
 * @date : 2022/10/13 16:59
 */
@Getter
@AllArgsConstructor
public enum UniExcelCellFieldTypeEnum {

    /**
     * 默认字符串类型
     */
    FIELD_STRING("FIELD_STRING", "字符串"),

    /**
     * 数值类型，整数或小数，
     * <p>
     * 备注：推荐使用，可以动态调整小数点位数
     */
    FIELD_NUMBER("FIELD_NUMBER", "数值"),

    /**
     * 整数
     */
    FIELD_INTEGER("FIELD_INTEGER", "整数"),

    /**
     * 小数，保留两位
     */
    FIELD_DOUBLE("FIELD_DOUBLE", "小数"),

    /**
     * 布尔类型，true/false
     */
    FIELD_BOOLEAN("FIELD_BOOLEAN", "布尔"),

    /**
     * 时间类型
     */
    FIELD_DATE("FIELD_DATE", "时间"),

    /**
     * 公式类型
     * <p>
     * 说明：2022-12-09 19:51 新增，该字段还未作兼容。
     */
    FIELD_FORMULA("FIELD_FORMULA", "公式");

    /**
     * 字段类型
     */
    private final String type;

    /**
     * 字段描述
     */
    private final String desc;

}
