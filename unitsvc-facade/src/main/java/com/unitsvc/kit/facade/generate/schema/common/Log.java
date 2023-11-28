package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：日志基表
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Log implements Serializable {
    private static final long serialVersionUID = 4925598137368210859L;
    /**
     * 同步标识
     */
    private String id;

    /**
     * 环境，目前支持dev/test/stg/prod四个环境
     */
    private String env;

    /**
     * 分区，如CN/US/EU
     */
    private String partition;

    /**
     * 状态，0：执行中，1：执行成功，2：执行异常
     */
    private int status;

    /**
     * 执行的总记录数
     */
    private int size;

    /**
     * 执行成功记录数
     */
    private int success;

    /**
     * 执行失败记录数
     */
    private int error;

    /**
     * 执行开始时间
     */
    private long timestamp;

    /**
     * 执行耗时（秒）
     */
    private Integer cost;

    /**
     * 错误信息
     */
    private String errorMessage;
}

