package com.github.IRedis.cache.api;

/*
 * 缓存明细
 */
public interface ICacheEntry<K, V> {
    /*
     * map键信息
     */
    K key();

    /*
     * map值信息
     */
    V value();
}
