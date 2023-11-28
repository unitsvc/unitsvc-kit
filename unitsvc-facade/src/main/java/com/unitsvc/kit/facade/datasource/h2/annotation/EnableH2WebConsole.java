package com.unitsvc.kit.facade.datasource.h2.annotation;

import com.unitsvc.kit.facade.datasource.h2.config.H2WebConsoleConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 功能描述：启用H2控制台
 * <p>
 * 说明：不建议开启，存在风险，仅开发阶段调试使用。
 * <pre>
 * 若已启用该注解，可在h2.properties配置中强制关闭。
 * h2.console.enabled=false
 * h2.webPort=8082
 * h2.webAdminPassword=admin
 * </pre>
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/21 13:55
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({H2WebConsoleConfig.class})
@Deprecated
public @interface EnableH2WebConsole {
}
