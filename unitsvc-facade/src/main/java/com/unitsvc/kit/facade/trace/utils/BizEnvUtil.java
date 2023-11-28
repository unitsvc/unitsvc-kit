package com.unitsvc.kit.facade.trace.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/26 14:44
 **/
public class BizEnvUtil {

    /**
     * 获取服务运行环境
     *
     * @return
     */
    public static String getAppRunEnv() {
        String env = System.getProperty("sccs", "default");
        if (StringUtils.isEmpty(env)) {
            return getK8sEnv();
        }
        return env;
    }

    /**
     * 获取k8s环境
     *
     * @return 环境
     */
    public static String getK8sEnv() {
        String k8sEnv = System.getenv("K8S_ENV");
        if (null != k8sEnv) {
            return k8sEnv;
        }
        return "";
    }

    /**
     * 获取k8s服务名称
     *
     * @return 服务名称
     */
    public static String getK8sServiceName() {
        String serviceName = System.getenv("SERVICE_NAME");
        if (null != serviceName) {
            return serviceName;
        }
        return "";
    }

    /**
     * 获取k8s实例名称
     *
     * @return 实例名称
     */
    public static String getK8sInstName() {
        String instName = System.getenv("INST_NAME");
        if (null != instName) {
            return instName;
        }
        return "";
    }

}
