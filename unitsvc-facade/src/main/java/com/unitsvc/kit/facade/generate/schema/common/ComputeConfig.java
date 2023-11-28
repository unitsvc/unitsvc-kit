package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：计算配置
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:02
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ComputeConfig implements Serializable {
    private static final long serialVersionUID = -8035842678212773352L;
    /**
     * 计算主表
     */
    private String source;

    /**
     * 计算关联表
     */
    private List<String> lookups;

    /**
     * 管道脚本，管道脚本会自动将lookup的表名转换成真实表名
     */
    private String pipeline;

    /**
     * 计算模块名
     */
    private String module;

    /**
     * 计算契约
     */
    private String secret;

    /**
     * 计算线程数，不大于0时复用任务线程
     */
    private int threads;

    /**
     * 计算目标表，通常为当前定义表本身，一般不需要设置，由计算模块自动处理
     */
    private String dest;
}
