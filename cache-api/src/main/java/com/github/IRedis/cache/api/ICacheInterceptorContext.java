package com.github.IRedis.cache.api;

import java.lang.reflect.Method;

public interface ICacheInterceptorContext<K, V> {

    ICache<K, V> cache();

    Method method();

    Object[] params();

    Object result();

    long startMills();

    long endMills();

}
