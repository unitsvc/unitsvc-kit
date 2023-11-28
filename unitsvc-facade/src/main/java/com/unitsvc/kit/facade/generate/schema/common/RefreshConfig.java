package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：数据刷新配置
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 22:55
 **/

@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
public class RefreshConfig implements Serializable {
    private static final long serialVersionUID = -4501979537525077213L;
    /**
     * 同步开关，只有当开关打开才会触发定时同步，手动同步不影响
     */
    @Deprecated
    private boolean sync;

    /**
     * 数据刷新频率，如果设置了该值，则数据会基于这个频率定时刷新
     */
    @Deprecated
    private String frequency;

    /**
     * 联动表集合，当前联动表发生全量更新时，会自动触发全量更新；
     * 如果是原生表，则只能选择中转站中的表；
     * 如果非原生表，则只能选择主库中的表；
     */
    @Deprecated
    private List<String> links;

    /**
     * 来源主表
     */
    @Deprecated
    private String source;

    /**
     * 管道脚本，管道脚本会自动将lookup的表名转换成真实表名
     */
    @Deprecated
    private String pipeline;

    /**
     * 刷新前的数据清理条件，默认为空则不清理
     */
    @Deprecated
    private String clean;

    /**
     * 批处理装置
     */
    private Batch batch;

    /**
     * 触发装置列表
     */
    private Map<String, Trigger> triggers;

    /**
     * 草稿状态的计算配置
     */
    @Deprecated
    private ComputeConfig draft;

    /**
     * 发布状态的刷新配置
     */
    private ComputeConfig release;

    /**
     * 刷新模式
     */
    private RefreshMode mode;

    /**
     * 批量触发装置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Batch implements Serializable {
        private static final long serialVersionUID = 8896090295208326531L;
        /**
         * 同步开关，只有当开关打开才会触发定时同步，手动同步不影响
         */
        private boolean sync;

        /**
         * 数据刷新频率，如果设置了该值，则数据会基于这个频率定时刷新
         */
        private String frequency;

        /**
         * 联动表集合，当前联动表发生全量更新时，会自动触发全量更新；
         * 如果是原生表，则只能选择中转站中的表；
         * 如果非原生表，则只能选择主库中的表；
         */
        private List<String> links;

        /**
         * 刷新前的数据清理条件，默认为空则不清理
         */
        private String clean;

        /**
         * 范围脚本，用于控制刷新范围逻辑
         */
        private String scope;
    }

    /**
     * 触发装置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class Trigger implements Serializable {
        private static final long serialVersionUID = -3446736645128507769L;
        /**
         * 同步开关
         */
        private boolean sync;

        /**
         * 触发事件类型列表
         */
        private List<TriggerType> types;

        /**
         * 范围脚本，用于控制刷新范围逻辑
         */
        private String scope;

    }

    /**
     * 事件类型定义
     */
    @Getter
    @AllArgsConstructor
    public enum TriggerType {
        /**
         * 插入事件
         */
        INSERT,

        /**
         * 更新事件
         */
        UPDATE,

        /**
         * 删除事件
         */
        DELETE
    }

    /**
     * 刷新模式
     */
    @AllArgsConstructor
    @Getter
    public enum RefreshMode {
        /**
         * 合并数据，只会将非空字段更新进数据
         */
        MERGE,

        /**
         * 覆盖数据，表定义中的字段全部更新（包括空字段会更新成null）
         */
        COVER,

        /**
         * 替换数据，直接调用底层替换方法，用新生成的数据代替原始数据，这个会导致非表定义中的字段会全部丢失
         */
        REPLACE
    }
}
