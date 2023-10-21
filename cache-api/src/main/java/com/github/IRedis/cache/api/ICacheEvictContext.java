package com.github.IRedis.cache.api;

public interface ICacheEvictContext<K, V> {

    /*
     * 新加的key
     */
    K key();

    /*
     * cache实现
     */
    ICache<K, V> cache();

    /*
     * 获取cache大小
     */
    int size();
}
