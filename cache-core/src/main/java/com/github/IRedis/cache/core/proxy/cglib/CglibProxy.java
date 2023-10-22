package com.github.IRedis.cache.core.proxy.cglib;
import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.core.proxy.ICacheProxy;
import com.github.IRedis.cache.core.proxy.bs.CacheProxyBs;
import com.github.IRedis.cache.core.proxy.bs.CacheProxyBsContext;
import com.github.IRedis.cache.core.proxy.bs.ICacheProxyBsContext;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;


public class CglibProxy implements MethodInterceptor, ICacheProxy {

    private final ICache target;

    public CglibProxy(ICache target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object o, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        ICacheProxyBsContext context = CacheProxyBsContext.newInstance()
                .method(method).params(params).target(target);
        return CacheProxyBs.newInstance().context(context).execute();
    }

    @Override
    public Object proxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }
}
