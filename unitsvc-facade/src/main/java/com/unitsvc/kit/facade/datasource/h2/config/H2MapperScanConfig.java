package com.unitsvc.kit.facade.datasource.h2.config;

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
@ComponentScan("com.unitsvc.kit.facade.datasource.h2.**")
@MapperScan(basePackages = "**.mapper.h2.**", sqlSessionTemplateRef = "h2SqlSessionTemplate")
@Component
@SuppressWarnings("all")
public class H2MapperScanConfig {
}
