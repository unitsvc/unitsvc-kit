package com.unitsvc.kit.facade.datasource.table.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：字段类型枚举
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 15:08
 **/
@AllArgsConstructor
@Getter
public enum ColumnTypeEnum {

    /**
     * 整型
     */
    INTEGER("整型", "INTEGER"),

    /**
     * 长整型
     */
    LONG("长整型", "BIGINT"),

    /**
     * 双精度浮点数
     */
    DOUBLE("双精度浮点数", "DOUBLE"),

    /**
     * 单精度浮点数
     */
    FLOAT("单精度浮点型", "FLOAT"),

    /**
     * 布尔型
     */
    BOOLEAN("布尔型", "BOOLEAN"),

    /**
     * 字符串
     */
    STRING("字符串", "VARCHAR"),

    /**
     * 数字
     */
    NUMBER("数字", "DECIMAL"),

    /**
     * 文本
     */
    TEXT("文本", "TEXT"),

    /**
     * 日期
     */
    DATE("日期", "DATE"),

    /**
     * 日期时间
     */
    DATETIME("日期时间", "DATETIME"),

    ;

    /**
     * 描述
     */
    private final String nameCn;

    /**
     * H2数据库字段类型映射
     */
    private final String h2JdbcType;

}
