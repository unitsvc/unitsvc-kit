package com.unitsvc.kit.core.diff.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：字段比对类型枚举
 * <p>
 * 说明：不包含对象类型
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/28 16:50
 **/
@Getter
@AllArgsConstructor
public enum DiffFieldTypeEnum {

    /**
     * 常规字节
     */
    BASE_BYTE("byte"),
    /**
     * 常规短整型
     */
    BASE_SHORT("short"),
    /**
     * 常规整型
     */
    BASE_INT("int"),
    /**
     * 常规长整型
     */
    BASE_LONG("long"),
    /**
     * 常规单精度浮点型
     */
    BASE_FLOAT("float"),
    /**
     * 常规双精度浮点型
     */
    BASE_DOUBLE("double"),
    /**
     * 常规布尔型
     */
    BASE_BOOLEAN("boolean"),
    /**
     * 常规字符型
     */
    BASE_CHAR("char"),
    /**
     * 布尔包装类型
     */
    WRAPPER_BOOLEAN("java.lang.Boolean"),
    /**
     * 字节包装类型
     */
    WRAPPER_BYTE("java.lang.Byte"),
    /**
     * 短整型包装类型
     */
    WRAPPER_SHORT("java.lang.Short"),
    /**
     * 整数包装类型
     */
    WRAPPER_INTEGER("java.lang.Integer"),
    /**
     * 长整型包装类型
     */
    WRAPPER_LONG("java.lang.Long"),
    /**
     * 单精度浮点包装类型
     */
    WRAPPER_FLOAT("java.lang.Float"),
    /**
     * 双精度浮点包装类型
     */
    WRAPPER_DOUBLE("java.lang.Double"),
    /**
     * 字符串
     */
    STRING("java.lang.String"),
    /**
     * 日期类型
     */
    DATE("java.util.Date"),
    /**
     * 高精度数字
     */
    BIG_DECIMAL("java.math.BigDecimal"),
    /**
     * 时间戳
     */
    SQL_TIMESTAMP("java.sql.Timestamp"),
    /**
     * JSON元素
     */
    JSON_ELEMENT("com.google.gson.JsonElement"),
    /**
     * JSON对象
     */
    JSON_OBJECT("com.google.gson.JsonObject"),
    /**
     * JSON常规
     */
    JSON_PRIMITIVE("com.google.gson.JsonPrimitive"),
    /**
     * JSON数组
     */
    JSON_ARRAY("com.google.gson.JsonArray"),
    ;
    /**
     * 类型名称
     */
    private final String typeName;

}
