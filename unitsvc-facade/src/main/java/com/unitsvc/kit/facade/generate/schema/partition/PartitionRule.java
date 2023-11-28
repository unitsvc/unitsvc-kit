package com.unitsvc.kit.facade.generate.schema.partition;

import com.unitsvc.kit.facade.generate.schema.common.BaseInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 功能描述：分区规则
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:13
 **/
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PartitionRule extends BaseInfo {
    /**
     * 内置员工分区规则，基于员工分区表，需要代码里特殊实现
     */
    public static final String PARTITION_EMPLOYEE = "partition_employee";
    /**
     * 参数
     */
    private List<String> params;

    /**
     * 分区表达式，返回值可为ALL及指定分区名，目前只支持单分区，TODO:后续可考虑返回多分区
     */
    private String expression;
}
