package com.unitsvc.kit.facade.datasource.table.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：数据表注解定义
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/29 18:05
 **/
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TableAnno {

    /**
     * 必填，数据表名
     *
     * @return 表名
     */
    String tableName() default "";

    /**
     * 可选，是否启用临时会话
     * <p>
     * 说明：若enableSession=true，启用临时会话，则额外新增会话字段，会话主键。
     *
     * @return
     */
    boolean enableSession() default false;

    /**
     * 可选，是否启用自动映射实体类，默认启用
     * <pre>
     * 说明：
     * 1.主键列注解必填：@ColumnAnno(isPrimary = true, columnName = "_id", columnAlias = "id", columnType = ColumnTypeEnum.STRING)
     * 2.启用自动映射后，其余自动自动映射
     * </pre>
     *
     * @return
     */
    boolean enableAuto() default true;

    /**
     * 可选，会话主键ID，会替换原表主键
     * <p>
     * 说明：若enableSession=true，启用临时会话，则创建会话主键字段，可修改字段名称。
     *
     * @return
     */
    String sessionKey() default "__primary_key__";

    /**
     * 可选，临时会话ID
     * <p>
     * 说明：若enableSession=true，启用临时会话，则创建会话ID，可修改字段名称。
     *
     * @return
     */
    String sessionId() default "__session_id__";

}
