package com.unitsvc.kit.core.diff.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：实体类对象比对配置
 * <p>
 * 说明：对应`@DiffField`注解，该功能待实现
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/28 17:48
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiffConfig implements Serializable {

    private static final long serialVersionUID = -654843405680407817L;

    /**
     * 实体类名称
     * <p>
     * 示例：TableInfo
     */
    private String simpleClassName;

    /**
     * 字段配置
     */
    private List<FieldConfig> fieldConfigs;

    /**
     * 字段配置
     */
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldConfig implements Serializable {

        private static final long serialVersionUID = 7357038451819553004L;

        /**
         * 特殊可选，是否主键
         * <p>
         * 说明：仅对集合生效，对应`@DiffId`注解，该功能待实现
         */
        private Boolean isPrimary;

        /**
         * 特殊可选，主键名称
         * <p>
         * 说明：仅对集合生效，对应`@DiffId`注解，该功能待实现
         */
        private String primaryName;

        /**
         * 必填，字段属性名称
         * <pre>
         * 示例：fields
         * </pre>
         */
        private String property;
        /**
         * 必填，中文名称
         */
        private String nameCn;
        /**
         * 可选，英文名称
         * <p>
         * 说明：暂不支持
         */
        private String nameEn;
        /**
         * 可选，是否忽略该字段，默认不忽略
         */
        private Boolean ignore;
        /**
         * 可选，字段格式化
         * <p>
         * 示例：yyyy-MM-dd HH:mm:ss
         */
        private String dataFormat;

    }
}
