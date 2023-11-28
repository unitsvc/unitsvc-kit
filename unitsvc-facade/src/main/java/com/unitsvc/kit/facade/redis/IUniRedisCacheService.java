package com.unitsvc.kit.facade.redis;

/**
 * 功能描述：redis通用缓存服务
 *
 * @author : jun
 * @version : v1.0.0
 * @date : 2022/10/27 15:41
 **/
public interface IUniRedisCacheService {

    /**
     * 资源加锁
     *
     * @param cacheKey       缓存键
     * @param threadUid      当前线程唯一UID
     * @param timeoutSeconds 超时时间，单位：秒。备注：超过指定时间，自动释放锁。
     * @return
     */
    boolean lock(String cacheKey, String threadUid, int timeoutSeconds);

    /**
     * 资源解锁
     *
     * @param cacheKey  缓存键
     * @param threadUid 当前线程唯一UID，防止释放非自己锁
     */
    void unLock(String cacheKey, String threadUid);

    /**
     * 设置临时缓存
     *
     * @param cacheKey      缓存键
     * @param cacheValue    缓存值
     * @param expireSeconds 过期时间
     */
    void setTempCache(String cacheKey, String cacheValue, int expireSeconds);

    /**
     * 获取临时缓存
     *
     * @param cacheKey 缓存键
     * @return 若缓存不存在，则返回null。
     */
    String getTempCache(String cacheKey);

    /**
     * 删除临时缓存
     *
     * @param cacheKey 缓存键
     * @return
     */
    Long delTempCache(String cacheKey);
}
