package com.github.IRedis.cache.core.persist;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICachePersist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class InnerCachePersist<K, V> {
    private static final Logger log = LoggerFactory.getLogger(InnerCachePersist.class);

    private ICache<K, V> cache;

    private ICachePersist<K, V> persist;

    private final static ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public InnerCachePersist(ICache<K, V> cache, ICachePersist<K, V> persist){
        this.cache = cache;
        this.persist = persist;
        this.init();
    }

    private void init(){
        EXECUTOR_SERVICE.scheduleAtFixedRate(() -> {
            try {
                log.info("开始持久化缓存信息");
                persist.persist(cache);
                log.info("完成持久化缓存信息");
            } catch (Exception exception) {
                log.error("文件持久化异常", exception);
            }
        }, 100, 100, TimeUnit.MILLISECONDS);
    }
}
