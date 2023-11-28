package com.unitsvc.kit.facade.trace.config;

import com.unitsvc.kit.facade.trace.utils.TraceConfigUtil;
import com.jpardus.spider.sccs.Log;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.annotation.PostConstruct;

/**
 * 功能描述：链路日志切面配置
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/26 14:32
 **/
@ComponentScan("com.unitsvc.kit.facade.trace.**")
@Configuration
@EnableAspectJAutoProxy
public class TraceAspectConfig {

    /**
     * 打印初始化配置记录
     */
    @PostConstruct
    public void debugTraceConfigLog() {
        String traceLogTableName = TraceConfigUtil.getTraceLogTableName();
        Log.debug(String.format("已开启链路追踪@BizLogTraceAnno注解功能，配置表名：%s，可以通过：TraceConfigUtil.setTraceLogTableName(\"custom_trace_collection_record_table\");方法全局修改。", traceLogTableName));
    }

}
