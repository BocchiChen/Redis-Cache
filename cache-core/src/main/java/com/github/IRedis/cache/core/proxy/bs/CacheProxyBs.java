package com.github.IRedis.cache.core.proxy.bs;

import com.github.IRedis.cache.annotation.CacheInterceptor;
import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheInterceptor;
import com.github.IRedis.cache.api.ICachePersist;
import com.github.IRedis.cache.core.interceptor.CacheInterceptorContext;
import com.github.IRedis.cache.core.interceptor.CacheInterceptors;
import com.github.IRedis.cache.core.persist.CachePersistAof;

import java.util.List;

public class CacheProxyBs {
    private CacheProxyBs(){}

    private ICacheProxyBsContext context;

    @SuppressWarnings("all")
    private final List<ICacheInterceptor> commonInterceptors = CacheInterceptors.defautCommonList();

    @SuppressWarnings("all")
    private final List<ICacheInterceptor> refreshInterceptors = CacheInterceptors.defautRefreshList();

    @SuppressWarnings("all")
    private final ICacheInterceptor persistInterceptors = CacheInterceptors.aof();

    @SuppressWarnings("all")
    private final ICacheInterceptor evictInterceptors = CacheInterceptors.evict();

    public static CacheProxyBs newInstance() {
        return new CacheProxyBs();
    }

    public CacheProxyBs context(ICacheProxyBsContext context){
        this.context = context;
        return this;
    }

    @SuppressWarnings("all")
    public Object execute() throws Throwable{
        final long startMills = System.currentTimeMillis();
        final ICache cache = context.target();
        CacheInterceptorContext interceptorContext = CacheInterceptorContext.newInstance()
                .startMills(startMills)
                .method(context.method())
                .params(context.params())
                .cache(context.target());
        CacheInterceptor cacheInterceptor = context.interceptor();
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, true);
        Object result = context.process();
        final long endMills = System.currentTimeMillis();
        interceptorContext.endMills(endMills).result(result);
        this.interceptorHandler(cacheInterceptor, interceptorContext, cache, false);
        return result;
    }

    @SuppressWarnings("all")
    public void interceptorHandler(CacheInterceptor cacheInterceptor,
                                   CacheInterceptorContext interceptorContext,
                                   ICache cache,
                                   boolean before){
        if(cacheInterceptor == null){
            return;
        }
        if(cacheInterceptor.common()){
            for(ICacheInterceptor interceptor : commonInterceptors) {
                if(before) {
                    interceptor.before(interceptorContext);
                } else {
                    interceptor.after(interceptorContext);
                }
            }
        }
        if(cacheInterceptor.refresh()){
            for(ICacheInterceptor interceptor : refreshInterceptors) {
                if(before) {
                    interceptor.before(interceptorContext);
                } else {
                    interceptor.after(interceptorContext);
                }
            }
        }

        final ICachePersist cachePersist = cache.persist();
        if(cacheInterceptor.aof() && (cachePersist instanceof CachePersistAof)) {
            if (before) {
                persistInterceptors.before(interceptorContext);
            } else {
                persistInterceptors.after(interceptorContext);
            }
        }
        if(cacheInterceptor.evict()) {
            if(before) {
                evictInterceptors.before(interceptorContext);
            } else {
                evictInterceptors.after(interceptorContext);
            }
        }
    }
}
