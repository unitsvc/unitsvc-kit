package com.unitsvc.kit.facade.redis.impl;

import com.jpardus.spider.fasten.Cache;
import com.jpardus.spider.fasten.StringCache;
import com.unitsvc.kit.facade.redis.IUniRedisCacheService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 功能描述：redis通用缓存服务
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/27 15:44
 **/
@Service
public class UniRedisCacheServiceImpl implements IUniRedisCacheService {

    /**
     * 默认命名空间
     */
    private static final String DEFAULT_NAMESPACE = "DEFAULT_NAMESPACE::";

    /**
     * 资源锁关键字
     */
    private static final String LOCK = "LOCK::";

    /**
     * 临时缓存关键字
     */
    private static final String TEMP_CACHE = "TEMP_CACHE::";

    /**
     * 资源加锁
     *
     * @param cacheKey       缓存键
     * @param threadUid      当前线程唯一UID
     * @param timeoutSeconds 超时时间，单位：秒。备注：超过指定时间，自动释放锁。
     * @return
     */
    @Override
    public boolean lock(String cacheKey, String threadUid, int timeoutSeconds) {
        // 完整缓存键
        String key = String.format("%s%s%s", DEFAULT_NAMESPACE, LOCK, cacheKey);
        // 获取锁
        Long lock = StringCache.setnx(key, threadUid);
        if (null != lock && lock > 0) {
            // 设置超时时间，防止一直占有锁不释放
            Cache.expire(key, timeoutSeconds);
            return true;
        }
        // 获取缓存键的过期时间
        Long ttl = Cache.ttl(key);
        if (null != ttl && ttl == -1) {
            // 由于过期时间不具有原子性，所以防止系统异常，导致锁一直被占用，做二次检验，然后设置过期时间。
            // 此处一般出现在系统宕机情况下
            Cache.expire(key, timeoutSeconds);
        }
        return false;
    }

    /**
     * 资源解锁
     *
     * @param cacheKey  缓存键
     * @param threadUid 当前线程唯一UID，防止释放非自己锁
     */
    @Override
    public void unLock(String cacheKey, String threadUid) {
        // 完整缓存键
        String key = String.format("%s%s%s", DEFAULT_NAMESPACE, LOCK, cacheKey);
        // 获取被锁线程UID
        String uuid = StringCache.get(key);
        if (!StringUtils.isEmpty(uuid)) {
            // 若当前线程与被锁线程相同，则释放锁
            if (uuid.equals(threadUid)) {
                Cache.del(key);
            }
        }
    }

    /**
     * 设置临时缓存
     *
     * @param cacheKey      缓存键
     * @param cacheValue    缓存值
     * @param expireSeconds 过期时间
     */
    @Override
    public void setTempCache(String cacheKey, String cacheValue, int expireSeconds) {
        // 完整缓存键
        String key = String.format("%s%s%s", DEFAULT_NAMESPACE, TEMP_CACHE, cacheKey);
        String num = StringCache.set(key, cacheValue);
        if (StringUtils.isNotEmpty(num)) {
            // 设置过期时间
            Cache.expire(key, expireSeconds);
        }
    }

    /**
     * 获取临时缓存
     *
     * @param cacheKey 缓存键
     * @return 若缓存不存在，则返回null。
     */
    @Override
    public String getTempCache(String cacheKey) {
        // 完整缓存键
        String key = String.format("%s%s%s", DEFAULT_NAMESPACE, TEMP_CACHE, cacheKey);
        return StringCache.get(key);
    }

    /**
     * 删除临时缓存
     *
     * @param cacheKey 缓存键
     * @return
     */
    @Override
    public Long delTempCache(String cacheKey) {
        // 完整缓存键
        String key = String.format("%s%s%s", DEFAULT_NAMESPACE, TEMP_CACHE, cacheKey);
        return Cache.del(key);
    }

}
