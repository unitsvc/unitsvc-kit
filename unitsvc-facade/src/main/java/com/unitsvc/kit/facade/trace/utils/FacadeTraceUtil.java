package com.unitsvc.kit.facade.trace.utils;

import com.google.common.collect.Lists;
import com.jpardus.spider.facade.model.Header;
import com.jpardus.spider.facade.server.Request;
import com.jpardus.spider.facade.server.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 功能描述：
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/28 11:28
 **/
public class FacadeTraceUtil {

    /**
     * 链路ID标识
     */
    private static final String FACADE_TRACE_ID_NAME = "_facade_trace_id__";
    /**
     * 请求地址
     */
    private static final String REQUEST_IP_NAME = "__ip__";
    /**
     * 请求源
     */
    private static final String REQUEST_ORIGIN = "origin";
    /**
     * 代理
     */
    private static final String REQUEST_USER_AGENT = "user-agent";
    /**
     * 内置字段
     */
    private static final List<String> INNER_FACADE_VAR = Lists.newArrayList("__api_key", "__service_token", "__timestamp");

    /**
     * 添加链路ID
     * <p>
     * 说明：RPC调用方执行
     */
    public static void addCookieTraceId() {
        Map<String, String> cookies = Request.getCookies();
        if (null != cookies) {
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (!INNER_FACADE_VAR.contains(key) && null != value) {
                    // rpc传递表头
                    Header.Client.gets().put(key, value);
                }
            }
        }
        Map<String, List<String>> headers = Request.getHeaders();
        if (null != headers) {
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                if (!INNER_FACADE_VAR.contains(key) && null != value) {
                    if (CollectionUtils.isNotEmpty(value)) {
                        // rpc传递表头
                        Header.Client.gets().put(key, value.get(0));
                    }
                }
            }
        }

        String address = Request.getAddress();
        if (StringUtils.isNotEmpty(address)) {
            Header.Client.gets().put(REQUEST_IP_NAME, address);
        }

        String traceId = Request.getCookie(FACADE_TRACE_ID_NAME);
        if (StringUtils.isEmpty(traceId)) {
            traceId = buildTraceId();
            Response.addCookie(FACADE_TRACE_ID_NAME, traceId);
        }
        Header.Client.gets().put(FACADE_TRACE_ID_NAME, traceId);
    }

    /**
     * 获取链路ID
     *
     * @return
     */
    public static String getTraceId() {
        String traceId = Request.getHeaderFirst(FACADE_TRACE_ID_NAME);
        if (StringUtils.isEmpty(traceId)) {
            traceId = (String) Header.ClientGetter.get(FACADE_TRACE_ID_NAME);
            if (StringUtils.isEmpty(traceId)) {
                traceId = buildTraceId();
                Response.addCookie(FACADE_TRACE_ID_NAME, traceId);
            }
        }
        return traceId;
    }

    /**
     * 请求地址
     *
     * @return
     */
    public static String getIp() {
        String ip = Request.getAddress();
        if (StringUtils.isEmpty(ip)) {
            ip = (String) Header.ClientGetter.get(REQUEST_IP_NAME);
        }
        return ip;
    }

    /**
     * 请求源
     *
     * @return
     */
    public static String getOrigin() {
        String origin = Request.getHeaderFirst(REQUEST_ORIGIN);
        if (StringUtils.isEmpty(origin)) {
            origin = (String) Header.ClientGetter.get(REQUEST_ORIGIN);
        }
        return origin;
    }

    /**
     * 代理
     *
     * @return
     */
    public static String getUserAgent() {
        String userAgent = Request.getHeaderFirst(REQUEST_USER_AGENT);
        if (StringUtils.isEmpty(userAgent)) {
            userAgent = (String) Header.ClientGetter.get(REQUEST_USER_AGENT);
        }
        return userAgent;
    }

    /**
     * 构建链路ID
     *
     * @return
     */
    private static String buildTraceId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
