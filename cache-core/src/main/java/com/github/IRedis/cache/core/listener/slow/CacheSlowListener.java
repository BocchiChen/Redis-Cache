package com.github.IRedis.cache.core.listener.slow;

import com.github.IRedis.cache.core.support.evict.CacheEvictLfu;
import com.google.gson.Gson;
import com.github.IRedis.cache.api.ICacheSlowListener;
import com.github.IRedis.cache.api.ICacheSlowListenerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheSlowListener implements ICacheSlowListener {

    private static final Logger log = LoggerFactory.getLogger(CacheEvictLfu.class);

    private static final Gson gson = new Gson();

    private long slowQueryMills = 1000L;

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn("[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(), gson.toJson(context.params()), context.costTimeMills());
    }

    @Override
    public long slowerThanMills() {
        return this.slowQueryMills;
    }

    @Override
    public void setSlowQueryMills(long timeMills) {
        this.slowQueryMills = timeMills;
    }
}
