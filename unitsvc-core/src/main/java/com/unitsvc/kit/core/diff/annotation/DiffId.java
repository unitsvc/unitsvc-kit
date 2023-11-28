package com.unitsvc.kit.core.diff.annotation;

import java.lang.annotation.*;

/**
 * 功能描述：集合主键注解
 * <p>
 * 说明：标记集合中对应的key,根据这个key来比对输出，仅对集合生效。
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/27 18:21
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DiffId {

    /**
     * 可选，集合主键标识，标记集合中对应的key,根据这个key来比对输出
     * <p>
     * 说明：若添加注解，则该字段必须有唯一值。
     *
     * @return
     */
    String name() default "id";

}
