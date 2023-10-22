package com.github.IRedis.cache.core.proxy.dymanic;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.core.proxy.ICacheProxy;
import com.github.IRedis.cache.core.proxy.bs.CacheProxyBs;
import com.github.IRedis.cache.core.proxy.bs.CacheProxyBsContext;
import com.github.IRedis.cache.core.proxy.bs.ICacheProxyBsContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy implements InvocationHandler, ICacheProxy {

    private final ICache target;

    public DynamicProxy(ICache target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        ICacheProxyBsContext context = CacheProxyBsContext.newInstance()
                .method(method).params(args).target(target);
        return CacheProxyBs.newInstance().context(context).execute();
    }

    @Override
    public Object proxy() {
        InvocationHandler handler = new DynamicProxy(target);
        return Proxy.newProxyInstance(handler.getClass().getClassLoader(),
                target.getClass().getInterfaces(), handler);
    }
}
