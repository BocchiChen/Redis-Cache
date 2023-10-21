package com.github.IRedis.cache.core.interceptor;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheInterceptorContext;

import java.lang.reflect.Method;

public class CacheInterceptorContext<K, V> implements ICacheInterceptorContext<K, V> {

    private ICache<K, V> cache;

    private Method method;

    private Object[] params;

    private Object result;

    private long startMills;

    private long endMills;

    public static <K, V> CacheInterceptorContext<K, V> newInstance(){
        return new CacheInterceptorContext<>();
    }

    public CacheInterceptorContext<K, V> cache(ICache<K, V> cache){
        this.cache = cache;
        return this;
    }

    @Override
    public ICache<K, V> cache() {
        return this.cache;
    }

    public CacheInterceptorContext<K, V> method(Method method){
        this.method = method;
        return this;
    }

    @Override
    public Method method() {
        return this.method;
    }

    public CacheInterceptorContext<K, V> params(Object[] params){
        this.params = params;
        return this;
    }

    @Override
    public Object[] params() {
        return new Object[0];
    }

    public CacheInterceptorContext<K, V> params(Object result){
        this.result = result;
        return this;
    }

    @Override
    public Object result() {
        return null;
    }

    public CacheInterceptorContext<K, V> startMills(long startMills){
        this.startMills = startMills;
        return this;
    }

    @Override
    public long startMills() {
        return this.startMills;
    }

    public CacheInterceptorContext<K, V> endMills(long endMills){
        this.endMills = endMills;
        return this;
    }

    @Override
    public long endMills() {
        return this.endMills;
    }
}
