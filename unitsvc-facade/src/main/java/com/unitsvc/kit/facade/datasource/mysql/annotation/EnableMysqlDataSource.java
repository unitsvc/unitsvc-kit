package com.unitsvc.kit.facade.datasource.mysql.annotation;

import com.unitsvc.kit.facade.datasource.mysql.config.MysqlDataSourceConfig;
import com.unitsvc.kit.facade.datasource.mysql.config.MysqlMapperScanConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 功能描述：启用Mysql数据源
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 9:29
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({MysqlDataSourceConfig.class, MysqlMapperScanConfig.class})
public @interface EnableMysqlDataSource {

}
