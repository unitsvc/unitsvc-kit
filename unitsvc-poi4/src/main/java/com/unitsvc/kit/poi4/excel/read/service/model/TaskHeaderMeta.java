package com.unitsvc.kit.poi4.excel.read.service.model;

import lombok.*;

import java.io.Serializable;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/12/9 13:06
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskHeaderMeta implements Serializable {

    private static final long serialVersionUID = -31878899300797111L;

    /**
     * 表头所在列序号，必填
     */
    private int headerIndex;

    /**
     * 表头中文名称，必填
     */
    private String nameCn;

    /**
     * 表头英文名称，必填
     * <p>
     * 说明：若是自定义表头列，格式，__headerIndex__，示例，__1__
     */
    private String nameEn;

    /**
     * 表头属性，必填
     * <p>
     * 说明：表明该表头列是系统预制还是自定义
     */
    private HeaderAttrEnum headerAttr;

    /**
     * 数据权限类型，必填
     * <p>
     * 说明：表明表头列数据权限
     */
    private DataPermEnum dataPerm;

    /**
     * 数据单元格类型
     * <p>
     * 说明：表明单元格数据类型，2022-12-09 14:50 初始版本约定，自定义类型默认为STRING
     */
    private DataCellEnum dataCell;

    /**
     * 定义表头属性
     */
    @Getter
    @AllArgsConstructor
    public enum HeaderAttrEnum implements Serializable {

        /**
         * 系统预配置
         */
        SYSTEM_CONFIG("系统预配置"),

        /**
         * 用户自定义
         */
        PEOPLE_CUSTOM("用户自定义");

        /**
         * 描述
         */
        private final String desc;

    }

    /**
     * 定义数据权限枚举
     */
    @Getter
    @AllArgsConstructor
    public enum DataPermEnum implements Serializable {

        /**
         * 编辑字段
         */
        EDIT("编辑"),

        /**
         * 展示字段
         */
        SHOW("展示");

        /**
         * 描述
         */
        private final String desc;

    }

    /**
     * 定义数据单元格类型
     */
    @Getter
    @AllArgsConstructor
    public enum DataCellEnum implements Serializable {

        /**
         * 字符串
         */
        STRING("字符串"),

        /**
         * 数字
         */
        NUMBER("数字"),

        /**
         * 整数
         */
        INTEGER("整数"),

        /**
         * 小数
         */
        DOUBLE("小数"),

        /**
         * 布尔
         */
        BOOLEAN("布尔"),

        /**
         * 时间
         */
        DATE("时间"),

        /**
         * 公式
         */
        FORMULA("公式"),

        /**
         * 未知
         */
        UNKNOWN("未知");

        /**
         * 描述
         */
        private final String desc;

    }

}
