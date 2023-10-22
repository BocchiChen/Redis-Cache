package com.github.IRedis.cache.core.proxy;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.core.proxy.cglib.CglibProxy;
import com.github.IRedis.cache.core.proxy.dymanic.DynamicProxy;
import com.github.IRedis.cache.core.proxy.none.NoneProxy;
import com.github.IRedis.cache.core.util.ObjectUtil;

import java.lang.reflect.Proxy;

public class CacheProxy {

    private CacheProxy(){}

    @SuppressWarnings("all")
    public static <K, V> ICache<K, V> getProxy(final ICache<K, V> cache){
        if(ObjectUtil.isNull(cache)){
            return (ICache<K, V>) new NoneProxy(cache).proxy();
        }
        final Class clazz = cache.getClass();
        if(clazz.isInterface() || Proxy.isProxyClass(clazz)){
            return (ICache<K, V>) new DynamicProxy(cache).proxy();
        }
        return (ICache<K, V>) new CglibProxy(cache).proxy();
    }
}
