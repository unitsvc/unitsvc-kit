package com.unitsvc.kit.facade.trace.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/26 18:22
 **/
@Getter
@AllArgsConstructor
public enum BizTraceStateEnum {

    /**
     * 执行中
     */
    RUNNING("执行中"),
    /**
     * 已完成
     */
    FINISHED("已完成"),
    /**
     * 超时
     */
    TIMEOUT("超时"),
    /**
     * 失败
     */
    FAILED("失败");

    /**
     * 中文名称
     */
    private final String nameCn;
}
