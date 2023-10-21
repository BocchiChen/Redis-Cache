package com.github.IRedis.cache.core.interceptor.aof;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheInterceptor;
import com.github.IRedis.cache.api.ICacheInterceptorContext;
import com.github.IRedis.cache.api.ICachePersist;
import com.github.IRedis.cache.core.model.PersistAofEntry;
import com.github.IRedis.cache.core.persist.CachePersistAof;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheInterceptorAof<K, V> implements ICacheInterceptor<K, V> {

    private static final Gson gson = new Gson();

    private static final Logger log = LoggerFactory.getLogger(CacheInterceptorAof.class);

    @Override
    public void before(ICacheInterceptorContext<K, V> context) {

    }

    @Override
    public void after(ICacheInterceptorContext<K, V> context) {
        ICache<K, V> cache = context.cache();
        ICachePersist<K, V> persist = cache.persist();
        if(persist instanceof CachePersistAof){
            CachePersistAof<K, V> cachePersistAof = (CachePersistAof<K, V>) persist;
            String methodName = context.method().getName();
            PersistAofEntry persistAofEntry = PersistAofEntry.newInstance();
            persistAofEntry.setMethodName(methodName);
            persistAofEntry.setParams(context.params());

            String json = gson.toJson(persistAofEntry);

            log.info("AOF 开始追加文件内容：{}", json);
            cachePersistAof.append(json);
            log.info("AOF 完成追加文件内容：{}", json);
        }
    }
}
