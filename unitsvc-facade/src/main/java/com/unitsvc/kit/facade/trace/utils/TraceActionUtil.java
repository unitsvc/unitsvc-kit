package com.unitsvc.kit.facade.trace.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.jpardus.spider.sccs.Log;
import com.unitsvc.kit.facade.trace.model.TraceAction;
import com.unitsvc.kit.facade.trace.model.TraceEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

/**
 * 功能描述：链路日志行为工具类
 * <p>
 * 说明：仅对接口方法添加@UniBizLogTraceAnno注解时生效
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/3/6 9:47
 **/
public class TraceActionUtil {

    /**
     * 当前及子线程用户信息
     */
    private static final ThreadLocal<TraceAction> TRACE_ACTION_TRACE_LOCAL = new InheritableThreadLocal<>();

    /**
     * 获取当前登录的行为用户
     *
     * @return
     */
    public static TraceAction getCurrentTraceAction() {
        return TRACE_ACTION_TRACE_LOCAL.get();
    }

    /**
     * 设置链路事件
     * <pre>
     * 说明：
     * 1.仅对接口实现方法添加@BizLogTraceAnno注解时生效。
     * 2.若在子线程中执行，则BizLogTraceAnno.asyncThreadCount>0，且调用TraceActionUtil.countDown()方法。
     * </pre>
     *
     * @param traceEvent 链路事件
     */
    public static void setTraceEvent(TraceEvent traceEvent) {
        // 获取当前链路用户
        TraceAction traceAction = getCurrentTraceAction();
        if (null != traceAction) {
            List<TraceEvent> traceEvents = Optional.ofNullable(traceAction.getTraceEvents()).orElse(new ArrayList<>());
            traceEvents.add(traceEvent);
            // 添加链路业务数据
            traceAction.setTraceEvents(traceEvents);
        } else {
            Log.error(String.format("当前链路日志无效，无法保存链路事件，请检查接口方法是否添加【@BizLogTraceAnno接口方法注解】，待保存的链路数据：%s", traceEvent));
        }
    }

    /**
     * 设置链路事件
     * <pre>
     * 说明：
     * 1.仅对接口实现方法添加@BizLogTraceAnno注解时生效。
     * 2.若在子线程中执行，则BizLogTraceAnno.asyncThreadCount>0，且调用TraceActionUtil.countDown()方法。
     * </pre>
     *
     * @param eventName 链路事件名称
     */
    public static TraceEvent setTraceEvent(String eventName) {
        TraceEvent traceEvent = new TraceEvent();
        traceEvent.setEventName(eventName);
        // 获取当前链路用户
        TraceAction traceAction = getCurrentTraceAction();
        if (null != traceAction) {
            List<TraceEvent> traceEvents = Optional.ofNullable(traceAction.getTraceEvents()).orElse(new ArrayList<>());
            traceEvents.add(traceEvent);
            // 添加链路业务数据
            traceAction.setTraceEvents(traceEvents);
        } else {
            Log.error(String.format("当前链路日志无效，无法保存链路事件，请检查接口方法是否添加【@BizLogTraceAnno接口方法注解】，待保存的链路数据：%s", traceEvent));
        }
        return traceEvent;
    }

    /**
     * 子线程计数减一
     * <p>
     * 警告：记录多线程日志必须使用。
     */
    public static void countDown() {
        // 获取当前链路用户
        TraceAction traceAction = getCurrentTraceAction();
        if (null != traceAction) {
            CountDownLatch countDownLatch = traceAction.getCountDownLatch();
            long count = countDownLatch.getCount();
            if (count > 0) {
                countDownLatch.countDown();
            }
        }
    }

    /**
     * 设置链路属性
     * <pre>
     * 说明：
     * 1.仅对接口实现方法添加@BizLogTraceAnno注解时生效。
     * 2.若在子线程中执行，则BizLogTraceAnno.asyncThreadCount>0，且调用TraceActionUtil.countDown()方法。
     * </pre>
     *
     * @param key   属性键
     * @param value 属性值
     */
    public static void setTraceExtendAttr(String key, JsonElement value) {
        // 获取当前链路用户
        TraceAction traceAction = getCurrentTraceAction();
        if (null != traceAction) {
            JsonObject bizExtend = traceAction.getBizExtend();
            bizExtend.add(key, value);
            // 添加链路业务数据
            traceAction.setBizExtend(bizExtend);
        } else {
            Log.error(String.format("当前链路日志无效，无法保存扩展字段，请检查接口方法是否添加【@BizLogTraceAnno接口方法注解】，待保存的扩展字段：%s->%s", key, value));
        }
    }

    /**
     * 设置线程数据
     * <p>
     * 说明：业务代码请勿调用
     *
     * @param traceAction 线程数据
     */
    public static void set(TraceAction traceAction) {
        TRACE_ACTION_TRACE_LOCAL.set(traceAction);
    }

    /**
     * 清理线程数据
     * <p>
     * 说明：业务代码请勿调用
     */
    public static void clear() {
        TRACE_ACTION_TRACE_LOCAL.remove();
    }

}
