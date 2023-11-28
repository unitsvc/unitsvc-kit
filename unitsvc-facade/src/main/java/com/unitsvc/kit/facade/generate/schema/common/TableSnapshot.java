package com.unitsvc.kit.facade.generate.schema.common;

import com.unitsvc.kit.facade.generate.schema.table.PermissionView;
import com.unitsvc.kit.facade.generate.schema.field.FieldConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：表快照
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:07
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TableSnapshot implements Serializable {
    private static final long serialVersionUID = -6244067878728930763L;
    /**
     * 数据列定义
     */
    private List<FieldConfig> columns;

    /**
     * 全量刷新配置
     */
    private RefreshConfig refresh;

    /**
     * 事件触发器列表
     */
    private List<TriggerConfig> triggers;

    /**
     * 授权视图列表，第一个为默认视图
     */
    private List<PermissionView> views;

}
