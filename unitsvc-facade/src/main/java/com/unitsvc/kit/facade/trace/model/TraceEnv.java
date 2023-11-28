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
 * @since : 2023/9/27 14:57
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TraceEnv implements Serializable {

    private static final long serialVersionUID = 715970651765947277L;

    /**
     * 运行环境
     */
    private String env;

    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 实例名称
     */
    private String instanceName;

}
