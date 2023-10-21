package com.github.IRedis.cache.core.proxy.bs;

import com.github.IRedis.cache.annotation.CacheInterceptor;
import com.github.IRedis.cache.api.ICache;

import java.lang.reflect.Method;

public class CacheProxyBsContext implements ICacheProxyBsContext{

    private ICache target;

    private Object[] params;

    private Method method;

    private CacheInterceptor interceptor;

    public CacheProxyBsContext newInstance(){
        return new CacheProxyBsContext();
    }

    @Override
    public CacheInterceptor interceptor() {
        return interceptor;
    }

    @Override
    public ICache target() {
        return target;
    }

    @Override
    public ICacheProxyBsContext target(ICache target) {
        this.target = target;
        return this;
    }

    public ICacheProxyBsContext params(Object[] params){
        this.params = params;
        return this;
    }

    @Override
    public Object[] params() {
        return this.params;
    }

    public ICacheProxyBsContext method(Method method){
        this.method = method;
        this.interceptor = method.getAnnotation(CacheInterceptor.class);
        return this;
    }

    @Override
    public Method method() {
        return this.method;
    }

    @Override
    public Object process() throws Throwable {
        return this.method.invoke(target, params);
    }
}
