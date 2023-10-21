package com.github.IRedis.cache.core.interceptor.evict;

import com.github.IRedis.cache.api.ICacheEvict;
import com.github.IRedis.cache.api.ICacheInterceptor;
import com.github.IRedis.cache.api.ICacheInterceptorContext;

public class CacheInterceptorEvict<K, V> implements ICacheInterceptor<K, V> {
    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
    }

    @Override
    @SuppressWarnings("all")
    public void after(ICacheInterceptorContext<K, V> context) {
        ICacheEvict<K, V> evict = context.cache().evict();
        String methodName = context.method().getName();
        final K key = (K) context.params()[0];
        if("remove".equals(methodName)){
            evict.removeKey(key);
        }else{
            evict.updateKey(key);
        }
    }
}
