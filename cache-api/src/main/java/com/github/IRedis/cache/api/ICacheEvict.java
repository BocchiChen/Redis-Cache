package com.github.IRedis.cache.api;

/*
 * 驱除策略
 */
public interface ICacheEvict<K, V> {
    /*
     * 驱除策略
     */
    ICacheEntry<K, V> evict(final ICacheEvictContext<K, V> context);

    /*
     * 更新key信息
     */
    void updateKey(K key);

    /*
     * 删除key信息
     */
    void removeKey(K key);
}
