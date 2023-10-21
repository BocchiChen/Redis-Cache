package com.github.IRedis.cache.core.interceptor.refresh;

import com.github.IRedis.cache.api.ICacheExpire;
import com.github.IRedis.cache.api.ICacheInterceptor;
import com.github.IRedis.cache.api.ICacheInterceptorContext;
import com.github.IRedis.cache.core.interceptor.aof.CacheInterceptorAof;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInterceptorRefresh<K, V> implements ICacheInterceptor<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorAof.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Refresh start");
        ICacheExpire<K, V> expire = context.cache().expire();
        expire.refreshExpire(context.cache().keySet());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {

    }
}
