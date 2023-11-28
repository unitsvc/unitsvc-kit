package com.unitsvc.kit.facade.datasource.table.annotation;

import com.unitsvc.kit.facade.datasource.table.enums.ColumnTypeEnum;

import java.lang.annotation.*;

/**
 * 功能描述：数据列注解定义
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/29 18:06
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ColumnAnno {

    /**
     * 可选，是否主键
     *
     * @return
     */
    boolean isPrimary() default false;

    /**
     * 必填，数据库列名
     * <p>
     * 说明：数据库字段名称
     *
     * @return
     */
    String columnName() default "";

    /**
     * 可选，数据列别名
     * <p>
     * 说明：实体类字段名称，若为空，则数据列名与数据列别名一致
     *
     * @return
     */
    String columnAlias() default "";

    /**
     * 必填，数据列类型
     * <p>
     * 说明：请谨慎设置数据列类型，本地持久化下，一旦确定就无法变更。
     */
    ColumnTypeEnum columnType() default ColumnTypeEnum.STRING;

}
