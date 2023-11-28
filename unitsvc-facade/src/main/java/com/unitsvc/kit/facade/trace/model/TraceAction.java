package com.unitsvc.kit.facade.trace.model;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述：登录行为用户信息
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/3/6 10:26
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TraceAction implements Serializable {
    private static final long serialVersionUID = -1128977733502853473L;
    /**
     * 链路ID
     */
    private String traceId;
    /**
     * 请求ID
     */
    private String requestId;
    /**
     * 请求时间
     */
    private Long traceTime;
    /**
     * 运行环境
     */
    private TraceEnv traceEnv;
    /**
     * 目标ID
     */
    private String targetId;
    /**
     * 子线程数
     * <p>
     * 说明：若大于1，则必须调用TraceActionUtil.countDown();方法
     */
    private CountDownLatch countDownLatch;
    /**
     * 异步线程数
     */
    private Integer asyncThreadCount;
    /**
     * 异步请求超时时间
     * <p>
     * 说明：单位分钟，默认30分钟，考虑脚本计算耗时，故默认设置30分钟。
     */
    private Integer asyncTimeoutMinute;
    /**
     * 请求头
     */
    private TraceHeader traceHeader;
    /**
     * 业务字段
     */
    private JsonObject bizExtend;
    /**
     * 链路事件
     */
    private List<TraceEvent> traceEvents;
}
