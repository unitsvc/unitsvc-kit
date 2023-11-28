package com.unitsvc.kit.facade.trace.aspect;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.*;
import com.unitsvc.kit.facade.trace.service.ITraceLogService;
import com.unitsvc.kit.facade.trace.service.pojo.BizTraceLog;

import com.jpardus.spider.facade.auth.Auth;
import com.jpardus.spider.facade.server.Facade;

import com.jpardus.spider.sccs.Log;
import com.jpardus.spider.sccs.utils.ObjectUtils;

import com.unitsvc.kit.facade.trace.anno.BizLogTraceAnno;
import com.unitsvc.kit.facade.trace.enums.BizTraceStateEnum;
import com.unitsvc.kit.facade.trace.model.TraceAction;
import com.unitsvc.kit.facade.trace.model.TraceEnv;
import com.unitsvc.kit.facade.trace.model.TraceHeader;
import com.unitsvc.kit.facade.trace.model.TraceRequest;
import com.unitsvc.kit.facade.trace.utils.*;
import org.apache.commons.lang.StringUtils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：业务注解实现
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/3/3 17:22
 **/
@Aspect
@Component
public class BizLogTraceAspect {

    private static final Gson GSON_NULL = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    /**
     * 链路日志服务
     */
    @Autowired
    private ITraceLogService traceLogService;

    @Pointcut("@annotation(com.unitsvc.kit.facade.trace.anno.BizLogTraceAnno)")
    public void pointcut() {
    }

    @Before("pointcut() && @annotation(annotation)")
    public void before(BizLogTraceAnno annotation) {
        // 设置线程变量数据
        this.setThreadLocalTraceAction(annotation);
    }

    @Around(value = "pointcut() && @annotation(annotation)")
    public Object around(ProceedingJoinPoint joinPoint, BizLogTraceAnno annotation) throws Throwable {
        // ------------------------------------ 正常业务调用 ----------------------------------
        try {
            // 执行方法
            return joinPoint.proceed();
            // 备注：不能捕获异常，因为可能是框架Response.error()抛出的异常。
        } finally {
            // pass
        }
    }

    /**
     * 框架内部中断异常
     */
    private static final String FACADE_INNER_EXCEPTION = "ResponseInterruptException";

    @AfterThrowing(pointcut = "pointcut() && @annotation(annotation)", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, BizLogTraceAnno annotation, Throwable e) {
        Log.error(String.format("接口请求异常：%s", e.getMessage()), e);
        // 创建日志记录
        BizTraceLog record = this.buildRecord(joinPoint, annotation);
        try {
            TraceAction currentTraceAction = TraceActionUtil.getCurrentTraceAction();
            if (null != currentTraceAction) {
                TraceRequest traceRequest = Optional.ofNullable(record.getTraceRequest()).orElse(new TraceRequest());
                // 记录错误信息
                traceRequest.setResult(false);
                if (e.getClass().getName().contains(FACADE_INNER_EXCEPTION)) {
                    traceRequest.setError("系统繁忙");
                } else {
                    traceRequest.setError(LogUtil.exceptionFirstLineStr(e));
                }
                traceRequest.setCost(System.currentTimeMillis() - currentTraceAction.getTraceTime());
            }
        } catch (Exception ex) {
            // pass
            record.setTraceError(LogUtil.exceptionFirstLineStr(ex));
            // pass
        } finally {
            // 记录链路日志，清除线程数据
            this.finishSaveTraceLogAndClearAsync(record);
        }
    }

    @AfterReturning(pointcut = "pointcut() && @annotation(annotation)")
    public void afterReturning(JoinPoint joinPoint, BizLogTraceAnno annotation) {
        // 创建日志记录
        BizTraceLog record = this.buildRecord(joinPoint, annotation);
        try {
            TraceAction traceAction = TraceActionUtil.getCurrentTraceAction();
            if (null != traceAction) {
                TraceRequest traceRequest = Optional.ofNullable(record.getTraceRequest()).orElse(new TraceRequest());
                // 记录成功信息
                traceRequest.setResult(true);
                traceRequest.setError("");
                traceRequest.setCost(System.currentTimeMillis() - traceAction.getTraceTime());
            }
        } catch (Exception e) {
            // pass
            record.setTraceError(LogUtil.exceptionFirstLineStr(e));
            // pass
        } finally {
            // 记录链路日志，清除线程数据
            this.finishSaveTraceLogAndClearAsync(record);
        }
    }

    private static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(5);

    /**
     * 记录链路日志
     *
     * @param record 记录数
     */
    private void finishSaveTraceLogAndClearAsync(BizTraceLog record) {
        // 获取当前线程信息
        TraceAction traceAction = TraceActionUtil.getCurrentTraceAction();
        FIXED_THREAD_POOL.submit(() -> {
            try {
                if (null != record && StringUtils.isNotEmpty(record.getId())) {
                    // 先记录当前任务
                    traceLogService.insert(record);
                    if (null != traceAction) {
                        CountDownLatch countDownLatch = traceAction.getCountDownLatch();
                        // 等待子线程结束，说明：由于脚本执行可能很久，默认30分钟超时
                        countDownLatch.await(traceAction.getAsyncTimeoutMinute(), TimeUnit.MINUTES);
                        TraceRequest traceRequest = Optional.ofNullable(record.getTraceRequest()).orElse(new TraceRequest());
                        // 更新业务执行时间
                        traceRequest.setCost(
                                System.currentTimeMillis() - traceAction.getTraceTime()
                        );
                        record.setTraceState(BizTraceStateEnum.FINISHED);
                    } else {
                        Log.debug(String.format("无法获取上下文链路信息：%s", record));
                    }
                }
            } catch (Exception e) {
                // pass
                Log.debug(String.format("链路日志处理异常：%s", e.getMessage()), e);
                record.setTraceError(String.format("%s", e.getMessage()));
                if (e instanceof InterruptedException) {
                    record.setTraceState(BizTraceStateEnum.TIMEOUT);
                } else {
                    record.setTraceState(BizTraceStateEnum.FAILED);
                }
            } finally {
                // 清除线程数据
                TraceActionUtil.clear();
                if (null != record) {
                    record.setUpdateTime(DateUtil.now());
                    // 记录操作日志
                    traceLogService.upsert(record);
                }
            }
        });
    }

    @After("pointcut() && @annotation(annotation)")
    public void afterFinally(JoinPoint joinPoint, BizLogTraceAnno annotation) {
        // pass
    }

    /**
     * 设置traceAction本地线程变量
     */
    private void setThreadLocalTraceAction(BizLogTraceAnno bizLogTraceAnno) {
        try {

            // 设置线程变量
            TraceActionUtil.set(
                    TraceAction.builder()
                            .requestId(UUID.randomUUID().toString().replace("-", ""))
                            .traceId(FacadeTraceUtil.getTraceId())
                            .traceTime(System.currentTimeMillis())
                            .traceEnv(
                                    TraceEnv.builder().env(BizEnvUtil.getAppRunEnv())
                                            .serviceName(BizEnvUtil.getK8sServiceName())
                                            .instanceName(BizEnvUtil.getK8sInstName())
                                            .build())
                            .traceHeader(
                                    TraceHeader.builder().ip(FacadeTraceUtil.getIp())
                                            .origin(FacadeTraceUtil.getOrigin())
                                            .userAgent(FacadeTraceUtil.getUserAgent())
                                            .build())
                            .targetId(Auth.getUser())
                            .bizExtend(new JsonObject())
                            .traceEvents(Lists.newArrayList())
                            .countDownLatch(new CountDownLatch(bizLogTraceAnno.asyncThreadCount()))
                            .asyncThreadCount(bizLogTraceAnno.asyncThreadCount())
                            .asyncTimeoutMinute(bizLogTraceAnno.asyncTimeoutMinute())
                            .build()
            );
        } catch (Exception e) {
            Log.error(String.format("设置TraceActionLog本地线程信息异常：%s", e.getMessage()), e);
        }
    }

    /**
     * 构建操作记录
     *
     * @param jointPoint 切面
     * @param annotation 注解
     * @return
     */
    public BizTraceLog buildRecord(JoinPoint jointPoint, BizLogTraceAnno annotation) {
        BizTraceLog record = new BizTraceLog();
        try {
            // 获取请求地址
            String requestUrl = this.buildRequestUrl(jointPoint);
            // 获取当前操作用户
            TraceAction traceAction = TraceActionUtil.getCurrentTraceAction();
            // 方法参数
            String methodParams = AspectUtil.getMethodParams(jointPoint);
            // 完整方法名称
            String methodName = AspectUtil.getMethodFullName(jointPoint);
            // 基本信息
            record.setId(IdUtil.getSnowflakeNextIdStr());
            record.setRequestId(traceAction.getRequestId());
            record.setTraceId(traceAction.getTraceId());
            record.setTraceTime(BizDateUtil.toFmtStrTime(traceAction.getTraceTime(), "yyyy-MM-dd HH:mm:ss"));
            record.setTraceEnv(traceAction.getTraceEnv());
            record.setTraceHeader(traceAction.getTraceHeader());
            record.setTraceState(BizTraceStateEnum.RUNNING);
            record.setTargetId(traceAction.getTargetId());
            record.setAsyncThreadCount(traceAction.getAsyncThreadCount());
            record.setAsyncTimeoutMinute(traceAction.getAsyncTimeoutMinute());
            record.setBizModule(annotation.bizModule());
            record.setBizDesc(annotation.bizDesc());
            record.setBizPage(annotation.bizPage());
            // --------------------------- 请求信息 ---------------------------
            TraceRequest traceRequest = new TraceRequest();
            if (StringUtils.isNotEmpty(methodParams)) {
                traceRequest.setParams(GSON_NULL.fromJson(methodParams, JsonElement.class));
            } else {
                traceRequest.setParams(new JsonPrimitive(""));
            }
            traceRequest.setMethod(methodName);
            traceRequest.setUrl(requestUrl);
            // 默认值
            traceRequest.setCost(null);
            // ---------------------------------------------------------------
            record.setTraceRequest(traceRequest);
            record.setCreateTime(DateUtil.now());
            // 设置业务事件
            record.setBizExtend(traceAction.getBizExtend());
            record.setTraceEvents(traceAction.getTraceEvents());
        } catch (Exception e) {
            // pass
        }
        return record;
    }

    /**
     * 构建接口请求参照
     *
     * @param jointPoint 切点
     * @return
     */
    public String buildRequestUrl(JoinPoint jointPoint) {
        try {
            Map<String, String> methodAndUrl = Maps.newConcurrentMap();
            Class<?> clazz = jointPoint.getTarget().getClass();
            String methodName = jointPoint.getSignature().getName();
            String key = clazz.getName() + "." + methodName;
            String query = methodAndUrl.get(key);
            if (ObjectUtils.isNotEmpty(query)) {
                return query;
            }
            if (ObjectUtils.isEmpty(methodAndUrl.get(key))) {
                Class<?>[] interfaces = clazz.getInterfaces();
                if (interfaces.length == 0) {
                    methodAndUrl.put(key, "#");
                    return "#";
                }
                Class<?> facadeInterface = null;
                for (Class<?> interfaceClass : interfaces) {
                    if (interfaceClass.isAnnotationPresent(Facade.class)) {
                        facadeInterface = interfaceClass;
                        break;
                    }
                }
                if (facadeInterface == null) {
                    methodAndUrl.put(key, "#");
                    return "#";
                }
                Facade facade = (Facade) facadeInterface.getAnnotation(Facade.class);
                String classRoute = facade.value();
                if (ObjectUtils.isEmpty(classRoute)) {
                    classRoute = facadeInterface.getSimpleName();
                }
                Method[] methods = facadeInterface.getDeclaredMethods();
                for (Method method : methods) {
                    String methodRoute = null;
                    if (method.isAnnotationPresent(Facade.class)) {
                        methodRoute = method.getAnnotation(Facade.class).value();
                    }
                    methodRoute = classRoute + "/" + ObjectUtils.ifEmpty(methodRoute, method.getName());
                    methodRoute = methodRoute.replaceAll("/{2,}", "/");
                    if (methodRoute.startsWith("/") && methodRoute.length() > 1) {
                        methodRoute = methodRoute.substring(1);
                    }
                    methodAndUrl.put(clazz.getName() + "." + method.getName(), methodRoute);
                }
            }
            return methodAndUrl.get(key);
        } catch (Exception e) {
            Log.error(String.format("获取请求地址异常：%s", e.getMessage()), e);
        }
        return "";
    }

}
