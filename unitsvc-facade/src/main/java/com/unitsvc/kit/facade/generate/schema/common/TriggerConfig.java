package com.unitsvc.kit.facade.generate.schema.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：事件触发配置
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 23:00
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class TriggerConfig implements Serializable {
    private static final long serialVersionUID = -5457160451124925425L;
    /**
     * 编号，历史记录基于此编号存储
     */
    private String id;

    /**
     * 同步开关
     */
    private boolean sync;

    /**
     * 关联表
     */
    @Deprecated
    private String table;

    /**
     * 事件类型，插入、更新、删除
     */
    @Deprecated
    private TriggerType type;

    /**
     * 处理管道，默认会传入修改前的值（<%b%>）及修改后的值（<%a%>）
     */
    @Deprecated
    private String pipeline;

    /**
     * 触发条件列表
     */
    @Deprecated
    private List<TriggerCondition> conditions;

    /**
     * 触发事件类型列表
     */
    private List<TriggerType> types;

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
     * 触发条件
     */
    @Deprecated
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class TriggerCondition implements Serializable {
        /**
         * 触发表
         */
        private String table;

        /**
         * 触发事件类型
         */
        private TriggerType type;
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
}
