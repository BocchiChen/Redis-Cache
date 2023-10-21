package com.github.IRedis.cache.core.interceptor.common;

import com.github.IRedis.cache.api.ICacheInterceptor;
import com.github.IRedis.cache.api.ICacheInterceptorContext;
import com.github.IRedis.cache.api.ICacheSlowListener;
import com.github.IRedis.cache.core.interceptor.aof.CacheInterceptorAof;
import com.github.IRedis.cache.core.listener.slow.CacheSlowListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CacheInterceptorCost<K, V> implements ICacheInterceptor<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorAof.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {
        log.debug("Cost start, method: {}", context.method().getName());
    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        long costMills = context.endMills() - context.startMills();
        final String methodName = context.method().getName();
        log.debug("Cost end, method: {}, cost: {}ms", methodName, costMills);

        List<ICacheSlowListener> listeners = context.cache().slowListeners();
        CacheSlowListenerContext listenerContext = CacheSlowListenerContext.newInstance()
                .startTimeMills(context.startMills())
                .endTimeMills(context.endMills())
                .costTimeMills(costMills)
                .params(context.params())
                .methodName(methodName)
                .result(context.result());

        for(ICacheSlowListener slowListener : listeners){
            long slowThanMills = slowListener.slowerThanMills();
            if(costMills >= slowThanMills){
                slowListener.listen(listenerContext);
            }
        }
    }
}
