package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.model.CacheEntry;

public class CacheEvictNone<K, V> extends AbstractCacheEvict<K, V> {

    @Override
    protected CacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        return null;
    }
}
