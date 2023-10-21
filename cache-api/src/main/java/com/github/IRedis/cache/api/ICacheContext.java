package com.github.IRedis.cache.api;

import java.util.Map;

/*
 * 缓存上下文
 */
public interface ICacheContext<K, V> {
    /*
     * 缓存map信息
     */
    Map<K, V> map();

    /*
     * 缓存map大小限制
     */
    int size();

    /*
     * 驱除策略
     */
    ICacheEvict<K, V> cacheEvict();
}
