package com.github.IRedis.cache.core.persist;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICachePersist;

import java.util.concurrent.TimeUnit;

public class CachePersistAdaptor<K, V> implements ICachePersist<K, V> {
    @Override
    public void persist(ICache<K, V> cache) {
    }

    @Override
    public long delay() {
        return this.period();
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }
}
