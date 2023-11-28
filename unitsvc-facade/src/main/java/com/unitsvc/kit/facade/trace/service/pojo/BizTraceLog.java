package com.unitsvc.kit.facade.trace.service.pojo;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import com.jpardus.db.jmongo.MongoId;

import com.unitsvc.kit.facade.trace.enums.BizTraceStateEnum;
import com.unitsvc.kit.facade.trace.model.TraceEnv;
import com.unitsvc.kit.facade.trace.model.TraceEvent;
import com.unitsvc.kit.facade.trace.model.TraceHeader;
import com.unitsvc.kit.facade.trace.model.TraceRequest;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/26 14:10
 **/

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BizTraceLog implements Serializable {
    private static final long serialVersionUID = 6604629373809462929L;

    /**
     * 主键id
     */
    @MongoId
    @SerializedName("_id")
    private String id;

    /**
     * 请求ID
     */
    private String requestId;

    /**
     * 必填，链路ID
     */
    private String traceId;

    /**
     * 必填，链路时间
     * <p>
     * 说明：格式化后的时间，yyyy-MM-dd HH:mm:ss
     */
    private String traceTime;

    /**
     * 必填，链路运行环境
     */
    private TraceEnv traceEnv;
    /**
     * 可选，请求头
     */
    private TraceHeader traceHeader;

    /**
     * 必填，链路状态
     */
    private BizTraceStateEnum traceState;

    /**
     * 必填，异步线程数
     */
    private Integer asyncThreadCount;

    /**
     * 必填，异步请求超时时间
     */
    private Integer asyncTimeoutMinute;

    /**
     * 可选，错误信息
     */
    private String traceError;

    /**
     * 目标ID
     */
    private String targetId;
    // ------------------------------------------- 注解字段 -----------------------------------------------------

    /**
     * 必填，业务模块【注解输入】
     */
    private String bizModule;

    /**
     * 必填，业务描述【注解输入】
     * <p>
     * 说明：描述业务功能意图
     */
    private String bizDesc;

    /**
     * 可选，页面类型【注解输入】
     */
    private String bizPage;

    // ------------------------------------------ 链路请求 -------------------------------------------

    private TraceRequest traceRequest;

    // -----------------------------------------------------------------------------------------------

    /**
     * 可选，业务扩展【业务自定义输入】
     * <pre>
     * 说明：
     * 1.业务扩展字段，简单对象存储。
     * 2.用于条件搜索
     * </pre>
     */
    private JsonElement bizExtend;

    /**
     * 特殊可选，数据变更事件集合
     * <pre>
     *  说明：记录业务事件
     * </pre>
     */
    private List<TraceEvent> traceEvents;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

}
