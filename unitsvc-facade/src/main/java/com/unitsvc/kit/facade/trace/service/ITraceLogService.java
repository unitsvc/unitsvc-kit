package com.unitsvc.kit.facade.trace.service;


import com.unitsvc.kit.facade.trace.service.pojo.BizTraceLog;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/27 13:28
 **/
public interface ITraceLogService {

    /**
     * 新增链路日志
     *
     * @param traceLog 链路日志
     */
    void insert(BizTraceLog traceLog);

    /**
     * 更新链路日志
     *
     * @param traceLog 链路日志
     */
    void upsert(BizTraceLog traceLog);

}
