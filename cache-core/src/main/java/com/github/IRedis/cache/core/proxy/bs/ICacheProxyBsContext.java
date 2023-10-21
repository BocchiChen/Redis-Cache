package com.github.IRedis.cache.core.proxy.bs;

import com.github.IRedis.cache.annotation.CacheInterceptor;
import com.github.IRedis.cache.api.ICache;

import java.lang.reflect.Method;

public interface ICacheProxyBsContext {
    CacheInterceptor interceptor();

    ICache target();

    ICacheProxyBsContext target(final ICache target);

    Object[] params();

    Method method();

    Object process() throws Throwable;
}
