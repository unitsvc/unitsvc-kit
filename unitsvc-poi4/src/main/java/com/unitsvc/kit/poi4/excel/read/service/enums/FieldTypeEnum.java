package com.unitsvc.kit.poi4.excel.read.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段类型 数值，字符串，时间
 *
 * @author jun
 */
@Getter
@AllArgsConstructor
public enum FieldTypeEnum {

    /**
     * 数值类型
     */
    NUMERICAL,

    /**
     * 字符串类型
     */
    STRING,

    /**
     * 布尔类型
     */
    BOOLEAN,

    /**
     * 日期类型
     * <p>
     * 备注：2023-01-12 17:39 新增该字段，该任务字段暂未配置
     */
    DATE;

}
