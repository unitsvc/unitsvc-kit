package com.unitsvc.kit.facade.generate.schema.field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：数据列定义配置
 * <p>
 * 说明：自动化工具帮助类
 *
 * @author : coder
 * @version : v1.0.0
 * @since : 2023/11/7 15:27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FieldConfig implements Serializable {

    private static final long serialVersionUID = -2255336126780061807L;

    /**
     * 数据列标识，在当前簇中必须唯一，当列标识为"id"时，表示该列为主键
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否敏感信息，用来告诉业务系统该字段要按敏感信息处理
     */
    private boolean sensitive;

    /**
     * 是否机密信息，如果是机密信息，在数据库中会以加密形式存储
     */
    private boolean confidential;

    /**
     * 标记该列必须有数据
     */
    private boolean required;

    /**
     * 是否是数组
     */
    private boolean array;

    /**
     * 数据类型
     */
    private ValueType type;

    /**
     * 字段外联表的标识，当字段为外联类型时有效
     */
    private String foreign;

    /**
     * 字段外联表展示字段，该字段值用于展示及检索，当字段为外联类型时有效
     */
    private String foreignShowField;

    /**
     * 枚举表达式，如："x,y,z"、"x|一,y|二,z|三"，当字段类型是枚举时有效
     */
    private String enums;

    /**
     * 子字段列表，当字段为Object类型时有效
     */
    private List<FieldConfig> fields;

    /**
     * 是否需要分区
     */
    private boolean partitionRequired;

    /**
     * 是否分区保护
     */
    private boolean partitionProtected;

    /**
     * 数据类型定义
     */
    @Getter
    @AllArgsConstructor
    public enum ValueType {
        /**
         * 字符串类型
         */
        STRING,

        /**
         * 判断类型
         */
        BOOLEAN,

        /**
         * 整型
         */
        INTEGER,

        /**
         * 浮点型
         */
        DOUBLE,

        /**
         * 枚举型
         */
        ENUM,

        /**
         * 对象
         */
        OBJECT,

        /**
         * 外联表
         */
        FOREIGN,

        /**
         * 时间类型
         */
        DATETIME
    }

}
