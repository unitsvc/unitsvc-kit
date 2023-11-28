package com.unitsvc.kit.facade.datasource.h2.config;

import com.jpardus.spider.sccs.Log;
import com.jpardus.spider.sccs.Properties;
import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

/**
 * 功能描述：H2控制台配置
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/6/21 13:56
 **/
public class H2WebConsoleConfig {

    /**
     * h2数据库配置
     */
    private static final Properties H2_PROPERTIES = Properties.loadProperties("h2");

    /**
     * 启用H2数据库控制台
     *
     * @return
     * @throws SQLException 异常
     */
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2WebServer() throws SQLException {
        // 此配置是强制关闭控制台，仅对false有效
        boolean enabled = H2_PROPERTIES.getValue("h2.console.enabled", true);
        if (enabled) {
            Log.warn("开启H2数据库控制台...，警告：仅适用于开发阶段调试，生产环境请勿开启。");
            String webPort = H2_PROPERTIES.getValue("h2.webPort", "8082");
            String webAdminPassword = H2_PROPERTIES.getValue("h2.webAdminPassword", "admin");
            Log.info(String.format("H2控制台访问地址：http://localhost:%s", webPort));
            return Server.createWebServer("-webPort", webPort, "-webAdminPassword", webAdminPassword, "-ifExists");
        }
        return null;
    }

}
