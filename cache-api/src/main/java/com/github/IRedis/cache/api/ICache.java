package com.github.IRedis.cache.api;

import java.util.List;
import java.util.Map;

/*
 * 缓存接口
 */
public interface ICache<K, V> extends Map<K, V> {
    /**
     * 设置缓存过期时间
     */
    ICache<K, V> expire(final K key, final long timeInMills);

    /**
     * 设置指定时间过期
     */
    ICache<K, V> expireAt(final K key, final long timeInMills);


    List<ICacheRemoveListener<K, V>> removeListeners();

    List<ICacheSlowListener> slowListeners();

    /**
     * 淘汰策略
     */
    ICacheEvict<K,V> evict();

    ICachePersist<K, V> persist();

    ICacheExpire<K, V> expire();

    ICacheLoad<K,V> load();

}
