package com.unitsvc.kit.core.field.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：字段类型枚举值
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/23 14:25
 **/
@Getter
@AllArgsConstructor
public enum UniFieldTypeEnum {

    /**
     * 字符串类型
     */
    PRIMITIVE_STRING("字符串", "PRIMITIVE_STRING"),

    /**
     * 布尔值类型
     * <p>
     * 说明：true|false
     */
    PRIMITIVE_BOOLEAN("布尔值", "PRIMITIVE_BOOLEAN"),

    /**
     * 整数类型
     * <p>
     * 说明：包含13位时间戳的时间格式类型
     */
    PRIMITIVE_INTEGER("整数", "PRIMITIVE_INTEGER"),

    /**
     * 小数类型
     */
    PRIMITIVE_DOUBLE("小数", "PRIMITIVE_DOUBLE"),

    /**
     * 日期类型
     * <p>
     * 说明：字符串格式的时间，示例：yyyy-MM-dd HH:mm:ss
     */
    PRIMITIVE_DATE("日期", "PRIMITIVE_DATE"),

    /**
     * 枚举类型
     * <p>
     * 说明：枚举字符串
     */
    PRIMITIVE_ENUM("枚举", "PRIMITIVE_ENUM"),

    /**
     * 对象类型
     */
    COMPLEX_OBJECT("对象", "COMPLEX_OBJECT"),

    /**
     * 数组类型
     */
    COMPLEX_ARRAY("数组", "COMPLEX_ARRAY"),

    ;

    /**
     * 中文名称
     */
    private final String nameCn;

    /**
     * 英文名称
     */
    private final String nameEn;

}
