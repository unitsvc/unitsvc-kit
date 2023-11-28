package com.unitsvc.kit.facade.trace.anno;

import java.lang.annotation.*;

/**
 * 功能描述：业务日志注解
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/3/3 17:14
 **/
@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BizLogTraceAnno {

    /**
     * 必填，业务模块
     *
     * @return
     */
    String bizModule() default "";

    /**
     * 必填，业务描述
     * <p>
     * 说明：描述业务功能或意图
     *
     * @return
     */
    String bizDesc() default "";

    /**
     * 必填。页面类型
     *
     * @return
     */
    String bizPage() default "";

    /**
     * 特殊可选，异步线程数标记，默认0同步
     * <pre>
     * 说明：
     * 1.asyncThreadCount=0，说明该方法为同步方法。
     * 2.asyncThreadCount=1,>1，说明该方法为异步方法。则必须调用TraceActionUtil.countDown()方法。
     * </pre>
     *
     * @return
     */
    int asyncThreadCount() default 0;

    /**
     * 特殊可选，异步请求超时时间
     * <p>
     * 说明：单位分钟，默认30分钟，考虑脚本计算耗时，故默认设置30分钟。
     *
     * @return
     */
    int asyncTimeoutMinute() default 30;

}
