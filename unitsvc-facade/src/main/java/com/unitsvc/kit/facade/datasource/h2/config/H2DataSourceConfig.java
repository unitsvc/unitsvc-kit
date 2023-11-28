package com.unitsvc.kit.facade.datasource.h2.config;

import com.alibaba.druid.DbType;
import com.alibaba.druid.pool.DruidDataSource;
import com.jpardus.spider.sccs.Log;
import com.jpardus.spider.sccs.Properties;
import com.mybatisflex.core.MybatisFlexBootstrap;
import com.mybatisflex.core.audit.AuditManager;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 功能描述：H2数据源配置
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/20 9:30
 **/

public class H2DataSourceConfig {

    /**
     * h2数据库配置
     */
    private static final Properties H2_PROPERTIES = Properties.loadProperties("h2");

    @Bean(name = "h2DruidDataSource")
    public DataSource h2DruidDataSource() {
        Log.debug("初始化H2数据库数据源...");
        String url = H2_PROPERTIES.getValue("h2.datasource.url", "jdbc:h2:file:./db/h2;mode=mysql;database_to_upper=false;non_keywords=user;file_lock=socket;auto_server=true;auto_reconnect=true;");
        String username = H2_PROPERTIES.getValue("h2.datasource.username", "root");
        String password = H2_PROPERTIES.getValue("h2.datasource.password", "root");
        DruidDataSource dataSource = new DruidDataSource();
        // 基本属性
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDbType(DbType.h2);
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

        try {
            dataSource.init();
        } catch (SQLException e) {
            Log.error(String.format("【初始化数据源异常】：%s", e.getErrorCode()), e);
        }
        return dataSource;
    }

    /**
     * 排除新增日志
     */
    private static final String EXCLUDE_INSERT = "INSERT";

    @PostConstruct
    public void initH2Config() throws Exception {
        Log.debug("初始化MybatisFlex启动配置...");
        // 开启审计功能
        AuditManager.setAuditEnable(true);
        // 设置 SQL 审计收集器
        AuditManager.setMessageCollector(auditMessage -> {
            if (!auditMessage.getFullSql().startsWith(EXCLUDE_INSERT)) {
                Log.debug(String.format("执行语句：%s 执行耗时：%sms", auditMessage.getFullSql(), auditMessage.getElapsedTime()));
            }
        });
        // 配置数据源
        MybatisFlexBootstrap.getInstance()
                .setDataSource(this.h2DruidDataSource())
                .start();
    }

    /**
     * 事务管理器
     *
     * @param dataSource 数据源
     * @return
     */
    @Bean(name = "h2TransactionManager")
    public DataSourceTransactionManager h2TransactionManager(@Qualifier("h2DruidDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    /**
     * 会话工厂
     *
     * @param dataSource 数据源
     * @return
     * @throws Exception
     */
    @Bean(name = "h2SqlSessionFactory")
    public SqlSessionFactory h2SqlSessionFactory(@Qualifier("h2DruidDataSource") DataSource dataSource) throws Exception {
        // 创建工厂bean的SqlSessionFactory
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        // 设置数据源
        bean.setDataSource(dataSource);
        // 加载映射文件
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mybatis/**/*.xml"));
        return bean.getObject();
    }

    /**
     * 会话模板
     *
     * @param sqlSessionFactory 会话工厂
     * @return
     * @throws Exception
     */
    @Bean(name = "h2SqlSessionTemplate")
    public SqlSessionTemplate h2SqlSessionTemplate(@Qualifier("h2SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
