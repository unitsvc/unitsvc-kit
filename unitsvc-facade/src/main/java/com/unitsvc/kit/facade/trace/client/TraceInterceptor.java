package com.unitsvc.kit.facade.trace.client;

import com.jpardus.spider.facade.client.IFacadeClientInterceptor;
import com.unitsvc.kit.facade.trace.utils.FacadeTraceUtil;

import java.lang.reflect.Method;

/**
 * 功能描述：链路追踪拦截器
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/9/28 13:38
 **/
public class TraceInterceptor implements IFacadeClientInterceptor {

    @Override
    public void beforeInvoke(Method method) {
        FacadeTraceUtil.addCookieTraceId();
    }

    @Override
    public void afterInvoke(Method method, Object o) {

    }
}
