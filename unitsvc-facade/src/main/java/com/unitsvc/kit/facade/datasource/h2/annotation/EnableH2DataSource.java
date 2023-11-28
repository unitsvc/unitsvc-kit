package com.unitsvc.kit.facade.datasource.h2.annotation;

import com.unitsvc.kit.facade.datasource.h2.config.H2MapperScanConfig;
import com.unitsvc.kit.facade.datasource.h2.config.H2DataSourceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 功能描述：启用H2数据源
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 9:29
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({H2DataSourceConfig.class, H2MapperScanConfig.class})
public @interface EnableH2DataSource {

}
