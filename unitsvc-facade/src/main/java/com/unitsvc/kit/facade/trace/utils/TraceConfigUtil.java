package com.unitsvc.kit.facade.trace.utils;

/**
 * 功能描述：链路追踪配置工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/10/24 10:17
 **/
public class TraceConfigUtil {

    /**
     * 链路日志表名称
     */
    private static String TRACE_LOG_TABLE_NAME = "uni_facade_trace_record";

    /**
     * 设置链路日志数据表名称
     * <p>
     * 说明：仅初始化一次，用于改变默认数据表
     *
     * @param tableName 数据表
     */
    public static void setTraceLogTableName(String tableName) {
        TRACE_LOG_TABLE_NAME = tableName;
    }

    /**
     * 获取链路日志数据表名称
     *
     * @return
     */
    public static String getTraceLogTableName() {
        return TRACE_LOG_TABLE_NAME;
    }
}
