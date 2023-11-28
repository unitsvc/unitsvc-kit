package com.unitsvc.kit.facade.datasource.clickhouse.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * 功能描述：组件扫描
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 10:43
 **/
@ComponentScan("com.unitsvc.kit.facade.datasource.clickhouse.**")
@MapperScan(basePackages = "**.mapper.clickhouse.**", sqlSessionTemplateRef = "clickhouseSqlSessionTemplate")
@Component
@SuppressWarnings("all")
public class ClickhouseMapperScanConfig {

}
