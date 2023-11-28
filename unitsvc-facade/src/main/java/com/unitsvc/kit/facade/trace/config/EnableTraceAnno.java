package com.unitsvc.kit.facade.trace.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 功能描述：启用链路注解
 * <p>
 * 说明：通过 TraceConfigUtil.setTraceLogTableName("cumstom_collection_table");修改配置
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/8 9:57
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(TraceAspectConfig.class)
public @interface EnableTraceAnno {

}
