package com.unitsvc.kit.facade.generate.schema.label;

import com.unitsvc.kit.facade.generate.schema.common.RefreshConfig;
import com.unitsvc.kit.facade.generate.schema.common.TriggerConfig;
import com.unitsvc.kit.facade.generate.schema.field.FieldConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：标签快照类
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:30
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LabelSnapshot implements Serializable {
    private static final long serialVersionUID = 3519469688752968909L;
    /**
     * 标签类型
     */
    private String type;

    /**
     * 关联数据列定义，用来标签限定，如限定某人为指定部门的BP
     */
    private List<FieldConfig> fields;

    /**
     * 刷新配置
     */
    private RefreshConfig pipeline;

    /**
     * 事件触发器列表
     */
    private List<TriggerConfig> triggers;
}
