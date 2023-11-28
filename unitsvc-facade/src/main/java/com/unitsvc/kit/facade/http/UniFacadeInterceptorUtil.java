package com.unitsvc.kit.facade.http;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.jpardus.spider.facade.server.Facade;
import com.jpardus.spider.facade.server.Request;
import com.jpardus.spider.sccs.Log;
import com.jpardus.spider.sccs.utils.ObjectUtils;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 功能描述：Facade框架拦截器工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/4/7 16:26
 **/
public class UniFacadeInterceptorUtil {

    /**
     * 展示空值-序列化
     */
    private static final Gson GSON_SHOW_NULL = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    /**
     * 过滤空值-序列化
     */
    private static final Gson GSON_FILTER_NULL = new GsonBuilder().disableHtmlEscaping().create();

    /**
     * 定义方法与路由映射
     */
    private static final Map<String, String> API_METHOD_ROUTE_MAPPING = new ConcurrentHashMap<>();

    /**
     * 获取方法与路由
     *
     * @param remote 拦截类
     * @param method 拦截方法
     * @return
     */
    public static MethodRouteAndArgs getMethodRouteAndArgs(Object remote, Method method, Object[] args) {
        try {
            if (null != remote) {
                Class<?> clazz = remote.getClass();
                // 获取方法键
                String classMethod = clazz.getName() + "." + method.getName();
                String requestRoute = buildRequestRoute(clazz, classMethod);
                JsonObject requestParams = buildRequestParamsToObj(method, args, false);
                String requestMethod = Request.getMethod();
                // 封装方法
                return MethodRouteAndArgs.builder()
                        .classMethod(classMethod)
                        .requestRoute(requestRoute)
                        .requestParams(requestParams)
                        .requestMethod(requestMethod).build();
            }
        } catch (Exception e) {
            // pass
            Log.debug(String.format("警告：解析请求参数异常：%s", e.getMessage()), e);
        }
        // 默认返回空值
        return MethodRouteAndArgs.builder().classMethod("").requestRoute("").requestParams(new JsonObject()).requestMethod("").build();
    }

    /**
     * 处理方法请求参数
     *
     * @param method 方法
     * @param args   参数
     * @return
     */
    public static JsonObject buildRequestParamsToObj(Method method, Object[] args, Boolean enableSerializeNull) {
        Parameter[] parameters = method.getParameters();
        // 组合请求参数
        if (parameters != null && parameters.length > 0) {
            // 请求参数
            Map<String, Object> params = new HashMap<>(parameters.length);
            try {
                // ------------------------------------- 处理单参数 -------------------------------------------------
                if (parameters.length == 1) {
                    if (null == args[0] || args[0] instanceof JsonNull) {
                        // 处理空值
                        JsonObject nullParams = new JsonObject();
                        nullParams.add(parameters[0].getName(), JsonNull.INSTANCE);
                        return nullParams;
                    }
                    // 文件上传类型
                    if ((args[0] instanceof byte[])) {
                        // 获取文件名称
                        String fileName = Request.getParameter(parameters[0].getName());
                        if (ObjectUtils.isNotEmpty(fileName)) {
                            // 注意此处无法序列化
                            params.put(parameters[0].getName(), Lists.newArrayList(fileName));
                        } else {
                            // 注意此处无法序列化
                            params.put(parameters[0].getName(), Lists.newArrayList());
                        }
                    } else {
                        if (isPrimitiveType(args[0]) || isList(args[0]) || isArray(args[0])) {
                            // 常规类型或数组
                            params.put(parameters[0].getName(), args[0]);
                        } else {
                            if (Boolean.TRUE.equals(enableSerializeNull)) {
                                // 对象类型
                                return GSON_SHOW_NULL.fromJson(GSON_SHOW_NULL.toJson(args[0]), JsonObject.class);
                            } else {
                                // 对象类型
                                return GSON_FILTER_NULL.fromJson(GSON_FILTER_NULL.toJson(args[0]), JsonObject.class);
                            }
                        }
                    }
                }
                // --------------------------------- 处理多请求参数 -------------------------------------------
                for (int i = 0; i < parameters.length; i++) {
                    // 文件上传类型
                    if ((args[i] instanceof byte[])) {
                        // 获取文件名称
                        String fileName = Request.getParameter(parameters[i].getName());
                        if (ObjectUtils.isNotEmpty(fileName)) {
                            // 注意此处无法序列化
                            params.put(parameters[i].getName(), Lists.newArrayList(fileName));
                        } else {
                            // 注意此处无法序列化
                            params.put(parameters[i].getName(), Lists.newArrayList());
                        }
                    } else {
                        params.put(parameters[i].getName(), args[i]);
                    }
                }
                if (params.size() != 0) {
                    if (Boolean.TRUE.equals(enableSerializeNull)) {
                        // 转成JSON字符串
                        return GSON_SHOW_NULL.fromJson(GSON_SHOW_NULL.toJson(params), JsonObject.class);
                    } else {
                        // 转成JSON字符串
                        return GSON_FILTER_NULL.fromJson(GSON_FILTER_NULL.toJson(params), JsonObject.class);
                    }
                }
            } finally {
                params.clear();
                params = null;
            }
        }
        return new JsonObject();
    }

    /**
     * 处理方法请求参数
     *
     * @param method 方法
     * @param args   参数
     * @return
     */
    public static String buildRequestParamsToStr(Method method, Object[] args, Boolean enableSerializeNull) {
        JsonObject params = buildRequestParamsToObj(method, args, enableSerializeNull);
        return GSON_SHOW_NULL.toJson(params);
    }

    /**
     * 处理方法路由名称
     *
     * @param clazz     方法类
     * @param methodKey 方法完整名称
     * @return
     */
    public static String buildRequestRoute(Class<?> clazz, String methodKey) {
        String url = API_METHOD_ROUTE_MAPPING.get(methodKey);
        if (ObjectUtils.isNotEmpty(url)) {
            return url;
        } else {
            Class<?>[] interfaces = clazz.getInterfaces();
            Class<?> facadeInterface = null;
            for (Class<?> interfaceClass : interfaces) {
                if (interfaceClass.isAnnotationPresent(Facade.class)) {
                    facadeInterface = interfaceClass;
                    break;
                }
            }
            if (facadeInterface == null) {
                API_METHOD_ROUTE_MAPPING.put(methodKey, "#");
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
                API_METHOD_ROUTE_MAPPING.put(clazz.getName() + "." + method.getName(), methodRoute);
            }
        }
        return API_METHOD_ROUTE_MAPPING.get(methodKey);
    }

    @Data
    @Builder
    public static class MethodRouteAndArgs implements Serializable {

        private static final long serialVersionUID = -4507505016914080208L;

        /**
         * 请求方法
         */
        private String requestMethod;

        /**
         * 请求路由
         */
        private String requestRoute;

        /**
         * 请求参数
         */
        private JsonObject requestParams;

        /**
         * 函数方法
         */
        private String classMethod;

        /**
         * 转换成json字符串
         *
         * @return
         */
        public String requestParamsToStr() {
            return GSON_SHOW_NULL.toJson(requestParams);
        }

    }

    /**
     * 判断是否常规类型
     *
     * @param obj 对象
     * @return
     */
    public static boolean isPrimitiveType(Object obj) {
        return obj instanceof String ||
                obj instanceof Integer ||
                obj instanceof Long ||
                obj instanceof Double ||
                obj instanceof Boolean ||
                obj instanceof Float ||
                obj instanceof Short ||
                obj instanceof Byte ||
                obj instanceof Character ||
                obj instanceof Enum ||
                obj instanceof JsonPrimitive
                ;
    }

    /**
     * 判断是否集合
     *
     * @param obj 对象
     * @return
     */
    public static boolean isList(Object obj) {
        return obj instanceof Collection || obj instanceof JsonArray;
    }

    /**
     * 判断是否数组
     *
     * @param obj 对象
     * @return
     */
    public static boolean isArray(Object obj) {
        if (null == obj) {
            return false;
        }
        return obj.getClass().isArray();
    }

}
