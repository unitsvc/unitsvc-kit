package com.unitsvc.kit.facade.trace.utils;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jpardus.spider.facade.server.Request;
import com.jpardus.spider.sccs.Log;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述：切面工具类
 * <p>
 * 说明：更新时间，2023/2/8 13:31 仅适用于Facade框架
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2023/2/8 12:45
 **/
public class AspectUtil {

    private static final Gson GSON_NULL = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    /**
     * 获取JSON格式的请求参数
     * <p>
     * 说明：2023/2/8 13:08 修改
     *
     * @param joinPoint 切点
     * @return
     */
    public static String getMethodParams(JoinPoint joinPoint) {
        // 请求参数
        String requestParams = "";
        // 获取请求参数
        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        // 获取请求参数类型
        String[] parameterNames = ms.getParameterNames();
        // 获取请求参数值
        Object[] parameterValues = joinPoint.getArgs();
        try {
            // 组合请求参数
            if (parameterNames != null && parameterNames.length > 0) {
                // 请求参数
                Map<String, Object> params = new HashMap<>(parameterNames.length);
                for (int i = 0; i < parameterNames.length; i++) {
                    // 文件上传类型
                    if ((parameterValues[i] instanceof byte[])) {
                        String fileName = Request.getParameter(parameterNames[i]);
                        if (StringUtils.isNotEmpty(fileName)) {
                            // 注意此处无法序列化
                            params.put(parameterNames[i], Lists.newArrayList(fileName));
                        } else {
                            // 注意此处无法序列化
                            params.put(parameterNames[i], Lists.newArrayList());
                        }
                    } else {
                        params.put(parameterNames[i], parameterValues[i]);
                    }
                }
                if (params.size() != 0) {
                    // 转成JSON字符串
                    requestParams = GSON_NULL.toJson(params);
                }
            }
        } catch (Exception e) {
            Log.error(String.format("【切面获取方法请求参数异常】，异常信息：%s", e.getMessage()), e);
        }
        return requestParams;
    }

    /**
     * 获取响应结果
     * <p>
     * 说明：2023/2/8 13:07 修改
     *
     * @param result 结果
     * @return
     */
    public static String getMethodResult(Object result) {
        // 请求操作成功
        String resultJsonString = "";
        if (result != null) {
            try {
                // 排除文件下载返回值
                if (result instanceof byte[]) {
                    return "[]";
                }
                // 转成JSON字符串
                resultJsonString = GSON_NULL.toJson(result);
            } catch (Exception e) {
                Log.error(String.format("【切面获取方法响应参数异常】，异常信息：%s", e.getMessage()), e);
                // 若出现错误，一般为序列化错误导致
                resultJsonString = String.valueOf(result);
            }
        }
        return resultJsonString;
    }

    /**
     * 获取堆栈字符串
     * <p>
     * 说明：2023/2/8 12:53 修改
     *
     * @param e 异常信息
     * @return
     */
    public static String stackTraceToString(Throwable e) {
        return stackTraceToString(e.getClass().getName(), e.getMessage(), e.getStackTrace());
    }

    /**
     * 转换异常信息为字符串
     * <p>
     * 说明：2023/2/8 12:52 修改
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     */
    private static String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuilder sb = new StringBuilder();
        sb.append(exceptionName)
                .append(":")
                .append(exceptionMessage)
                .append("\n\t");
        for (StackTraceElement stet : elements) {
            sb.append(stet).append("\n\t");
        }
        return sb.toString();
    }

    /**
     * 获取接口方法完整名称
     * <p>
     * 说明：2023/2/8 12:52 修改
     *
     * @param joinPoint 切点
     * @return
     */
    public static String getMethodFullName(JoinPoint joinPoint) {
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的完整方法名
        return className + "." + method.getName();
    }

    /**
     * 获取自定义切面注解
     * <p>
     * 说明：2023/2/8 12:50 修改
     *
     * @param joinPoint             切点
     * @param customAnnotationClazz 自定义注解类
     * @param <CustomAnnotation>    自定义注解
     * @return 自定义注解实体类
     */
    public static <CustomAnnotation extends Annotation> CustomAnnotation getCustomAnnotation(JoinPoint joinPoint, Class<CustomAnnotation> customAnnotationClazz) {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            return method.getAnnotation(customAnnotationClazz);
        }
        return null;
    }

}
