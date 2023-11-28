package com.unitsvc.kit.facade.datasource.clickhouse.annotation;

import com.unitsvc.kit.facade.datasource.clickhouse.config.ClickhouseDataSourceConfig;
import com.unitsvc.kit.facade.datasource.clickhouse.config.ClickhouseMapperScanConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 功能描述：启用Clickhouse数据源
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 9:29
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ClickhouseDataSourceConfig.class, ClickhouseMapperScanConfig.class})
public @interface EnableClickhouseDataSource {

}
