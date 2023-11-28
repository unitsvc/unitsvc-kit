package com.unitsvc.kit.facade.trace.model;

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
 * @since : 2023/9/26 14:54
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class TraceHeader implements Serializable {
    private static final long serialVersionUID = 5752112733707953275L;
    /**
     * 访问IP
     */
    private String ip;

    /**
     * 请求站点
     */
    private String origin;

    /**
     * 用户代理
     */
    private String userAgent;
}
