package com.unitsvc.kit.facade.generate.schema.label;

import com.unitsvc.kit.facade.generate.schema.field.FieldConfig;
import com.unitsvc.kit.facade.generate.schema.common.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 功能描述：标签信息
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:33
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class LabelInfo extends BaseInfoExtend {
    /**
     * 归属分组编号
     */
    private String group;

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

    /**
     * 标签所有者，所有者可以管理标签自定义对象
     */
    private List<String> owners;

    /**
     * 标签所有者（草稿），所有者可以管理标签自定义对象
     */
    @Deprecated
    private List<String> draftOwners;

    /**
     * 计算配置是否审核中
     */
    @Deprecated
    private boolean computeChecking;

    /**
     * 触发配置是否审核中
     */
    @Deprecated
    private boolean triggerChecking;

    /**
     * 授权是否审核中
     */
    @Deprecated
    private boolean permissionChecking;

    /**
     * 快照
     */
    private SnapshotContainer<LabelSnapshot> snapshot;

    /**
     * 标签所有者审核控制
     */
    private ApproveContainer<List<String>> approvedOwners;

    /**
     * 下线审核控制
     */
    private ApproveContainer approveOffline;

}
