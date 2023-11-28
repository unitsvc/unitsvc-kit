package com.unitsvc.kit.poi4.excel.read.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字段格式化枚举
 * <p>
 * 备注：旧的任务字段定义，继续使用中，此字段主要用于控制前端交互，数据展示、数据输入
 *
 * @author jun
 */
@Getter
@AllArgsConstructor
public enum FieldFormatEnum {

    /**
     * 数字，前端展示数字，输入任意数字
     */
    NUMERICAL("数字"),

    /**
     * 单选，前端展示单选，单选按钮输入
     * <p>
     * 示例：【是|true,否|false】【0.1|0.1,0.2|0.2】
     */
    RADIO("单选"),

    /**
     * 多选，前端展示多选，多选按钮输入
     */
    CHECKBOX("多选"),

    /**
     * 日期，前端展示日期，日期组件选定时间输入
     */
    DATE("日期"),

    /**
     * 搜索，前端下拉搜索，选定一个值输入
     */
    SEARCH("单选"),

    /**
     * 搜索，前端下拉搜索，选定多个值输入
     */
    MULTSEARCH("多选"),

    /**
     * 文本，前端展示文本，输入任意文本
     */
    STRING("文本");

    /**
     * 详细描述
     */
    private final String details;

}