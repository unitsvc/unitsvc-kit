package com.unitsvc.kit.facade.generate.schema.table;

import com.unitsvc.kit.facade.generate.schema.common.*;
import com.unitsvc.kit.facade.generate.schema.partition.PartitionRuleInst;
import com.unitsvc.kit.facade.generate.schema.field.FieldConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：数据对象定义
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 22:50
 **/
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TableInfo extends BaseInfoExtend {
    /**
     * 归属数据集标识，用于归类及查询
     */
    private String group;

    /**
     * 最后更新时间
     */
    private long lastRefreshTime;

    /**
     * 是否原生数据；
     * 原生数据支持配置增删改权限及中转站批量导入配置；
     * 再生数据只支持配置查看权限、批量导入配置及事件导入配置；
     */
    private boolean origin;

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

    /**
     * 授权视图列表（草稿），第一个为默认视图
     */
    @Deprecated
    private List<PermissionView> draftViews;

    /**
     * 表格所有者，所有者可以管理表（数据门户中）
     */
    private List<String> owners;

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
     * 备份配置
     */
    private BackupConfig backup;

    /**
     * 快照
     */
    private SnapshotContainer<TableSnapshot> snapshot;

    /**
     * 表格所有者审批控制
     */
    private ApproveContainer<List<String>> approvedOwners;

    /**
     * 备份配置审核控制
     */
    private ApproveContainer<BackupConfig> approveBackup;

    /**
     * 下线审核控制
     */
    private ApproveContainer approveOffline;

    /**
     * 分区规则配置
     */
    private PartitionRuleInst partitionRuleInst;

    /**
     * 备份策略配置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class BackupConfig implements Serializable {
        private static final long serialVersionUID = -5466565185541389059L;
        /**
         * 备份开关
         */
        private boolean on;

        /**
         * 是否增量备份，此开关打开时，当发生数据更新时自动进行备份
         */
        private boolean increment;

        /**
         * 非增量备份时有效，定时备份全量数据
         */
        private String frequency;
    }
}
