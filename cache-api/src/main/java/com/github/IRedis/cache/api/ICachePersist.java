package com.github.IRedis.cache.api;

import java.util.concurrent.TimeUnit;

public interface ICachePersist<K, V> {

    void persist(ICache<K, V> cache);

    long delay();

    long period();

    TimeUnit timeUnit();
}
