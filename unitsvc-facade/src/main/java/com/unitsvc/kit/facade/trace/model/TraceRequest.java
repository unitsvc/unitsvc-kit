package com.unitsvc.kit.facade.trace.model;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/27 11:37
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TraceRequest implements Serializable {

    private static final long serialVersionUID = 2372104705210915399L;
    /**
     * 请求结果
     * <p>
     * 说明：2023/3/9 补充字段
     */
    private Boolean result;

    /**
     * 错误提示
     * <p>
     * 说明：2023/3/9 补充字段
     */
    private String error;

    /**
     * 必填，操作方法
     */
    private String method;

    /**
     * 必填，请求参数
     */
    private JsonElement params;

    /**
     * 必填，请求路径
     */
    private String url;

    /**
     * 必填，请求时长
     * <p>
     * 说明：单位毫秒
     */
    private Long cost;
}
