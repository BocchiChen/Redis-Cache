package com.github.IRedis.cache.core.proxy.none;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.core.proxy.ICacheProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class NoneProxy implements InvocationHandler, ICacheProxy {

    private final ICache target;

    public NoneProxy(ICache target) {
        this.target = target;
    }

    @Override
    public Object proxy() {
        return this.target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(proxy, args);
    }
}
