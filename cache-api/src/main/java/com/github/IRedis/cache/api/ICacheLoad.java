package com.github.IRedis.cache.api;

import java.lang.reflect.InvocationTargetException;

public interface ICacheLoad<K, V> {
    void load(final ICache<K, V> cache) throws InvocationTargetException, IllegalAccessException;
}
