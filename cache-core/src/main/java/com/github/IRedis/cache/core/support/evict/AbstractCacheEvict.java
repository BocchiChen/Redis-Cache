package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.api.ICacheEvict;
import com.github.IRedis.cache.api.ICacheEvictContext;

public abstract class AbstractCacheEvict<K, V> implements ICacheEvict<K, V> {

    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        return doEvict(context);
    }

    /*
     * 执行驱除策略
     */
    protected abstract ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context);

    @Override
    public void updateKey(K key) {

    }

    @Override
    public void removeKey(K key) {

    }
}
