package com.unitsvc.kit.core.diff.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：日志注解
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 18:21
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DiffField {

    /**
     * 必填，中文名称
     *
     * @return name
     */
    String name();

    /**
     * 可选，时间格式化，默认为空
     * <p>
     * 示例：yyyy-MM-dd HH:mm:ss
     *
     * @return dateFormat
     */
    String dateFormat() default "";

    /**
     * 可选，是否忽略该字段
     *
     * @return ignore
     */
    boolean ignore() default false;

}
