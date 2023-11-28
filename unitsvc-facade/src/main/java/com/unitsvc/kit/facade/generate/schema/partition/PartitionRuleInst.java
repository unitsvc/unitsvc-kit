package com.unitsvc.kit.facade.generate.schema.partition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * 功能描述：分区规则实例
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:10
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PartitionRuleInst implements Serializable {
    /**
     * 分区模式
     */
    private PartitionMode mode;
    /**
     * 分区规则编号
     */
    private String rule;

    /**
     * 参数映射，为空则默认同名
     */
    private Map<String, String> params;
}
