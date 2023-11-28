package com.unitsvc.kit.facade.utils;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.gson.JsonObject;
import com.jpardus.spider.sccs.Log;

import java.util.concurrent.TimeUnit;

/**
 * 功能描述：本地缓存工具类
 *
 * @author : jun
 * @version : v1.0.0
 * @since : 2023/11/7 22:34
 **/
public class UniLocalCacheUtil {

    /**
     * 本地缓存
     */
    public static final Cache<String, JsonObject> KEY_OBJ_CACHE = Caffeine
            .newBuilder()
            // 开启记录
            .recordStats()
            // 指定初始大小
            .initialCapacity(100)
            // 超出最大容量时淘汰
            .maximumSize(300)
            // 淘汰监听
            .removalListener(((key, value, cause) -> {
                Log.debug(String.format("【缓存过期】缓存键：[%s]，原因：%s", key, cause));
            }))
            // 写缓存30秒后过期，由于配置会改变故缓存时间不能太大
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();
}
