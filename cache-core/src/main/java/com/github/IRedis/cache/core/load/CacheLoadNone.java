package com.github.IRedis.cache.core.load;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheLoad;

public class CacheLoadNone<K, V> implements ICacheLoad<K, V> {
    @Override
    public void load(ICache<K, V> cache) {

    }
}
