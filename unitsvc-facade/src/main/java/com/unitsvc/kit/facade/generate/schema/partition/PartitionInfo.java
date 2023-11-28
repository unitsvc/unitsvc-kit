package com.unitsvc.kit.facade.generate.schema.partition;

import com.unitsvc.kit.facade.generate.schema.common.BaseInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 功能描述：分区信息
 * <pre>
 *  id为英文名
 *  name为中文名
 * </pre>
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:16
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class PartitionInfo extends BaseInfo {
}
