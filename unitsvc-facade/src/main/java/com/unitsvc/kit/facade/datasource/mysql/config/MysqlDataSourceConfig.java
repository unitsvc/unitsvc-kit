package com.unitsvc.kit.facade.datasource.mysql.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.jpardus.spider.sccs.Log;
import com.jpardus.spider.sccs.Properties;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 功能描述：mysql数据源配置
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 11:03
 **/
public class MysqlDataSourceConfig {

    /**
     * MYSQL数据库配置
     */
    private static final Properties MYSQL_PROPERTIES = Properties.loadProperties("mysql");

    /**
     * 数据源
     *
     * @return
     */
    @Bean(name = "mysqlDataSource")
    public DataSource mysqlDataSource() {
        Log.debug("初始化H2数据库数据源...");

        // 默认采用内置H2数据库
        String url = MYSQL_PROPERTIES.getValue("mysql.datasource.url", "jdbc:h2:file:./db/h2;mode=mysql;database_to_upper=false;non_keywords=user;file_lock=socket;auto_server=true;auto_reconnect=true;");
        String username = MYSQL_PROPERTIES.getValue("mysql.datasource.username", "root");
        String password = MYSQL_PROPERTIES.getValue("mysql.datasource.password", "root");

        DruidDataSource dataSource = new DruidDataSource();
        // 基本属性
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        // 配置初始化大小、最小、最大
        dataSource.setInitialSize(10);
        dataSource.setMinIdle(20);
        dataSource.setMaxActive(60);
        // 配置获取连接等待超时的时间
        dataSource.setMaxWait(60000);
        // 配置获取连接等待超时的时间
        dataSource.setTimeBetweenEvictionRunsMillis(60000);
        // 配置一个连接在池中最小生存的时间，单位是毫秒
        dataSource.setMinEvictableIdleTimeMillis(300000);
        dataSource.setValidationQuery("SELECT 1 FROM DUAL;");
        dataSource.setTestWhileIdle(true);
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);

        try {
            dataSource.init();
        } catch (SQLException e) {
            Log.error(String.format("【初始化数据源异常】：%s", e.getErrorCode()), e);
        }
        return dataSource;
    }

    /**
     * 事务管理器
     *
     * @param dataSource 数据源
     * @return
     */
    @Bean(name = "mysqlTransactionManager")
    public DataSourceTransactionManager mysqlTransactionManager(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 会话工厂
     *
     * @param dataSource 数据源
     * @return
     * @throws Exception
     */
    @Bean(name = "mysqlSqlSessionFactory")
    public SqlSessionFactory mysqlSqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource) throws Exception {
        // 创建工厂bean的SqlSessionFactory
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        // 设置数据源
        bean.setDataSource(dataSource);
        // 加载映射文件
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mybatis/**/*.xml"));
        // 设置插件
        bean.setPlugins(this.mysqlMybatisPlusInterceptor());
        return bean.getObject();
    }

    /**
     * 会话模板
     *
     * @param sqlSessionFactory 会话工厂
     * @return
     * @throws Exception
     */
    @Bean(name = "mysqlSqlSessionTemplate")
    public SqlSessionTemplate mysqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    public MybatisPlusInterceptor mysqlMybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

}
