package com.unitsvc.kit.facade.generate.schema.partition;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能描述：表分区模式，当表分区为固定分区时，模式是无用的
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:12
 **/
@AllArgsConstructor
@Getter
public enum PartitionMode {
    /**
     * 行级分区，整行分区存储，此时列上的分区配置无效
     */
    ROW,

    /**
     * 列级分区，各分区行数相等，列上的分区配置生效
     */
    COLUMN
}
