package com.github.IRedis.cache.api;

public interface ICacheSlowListener {
    void listen(final ICacheSlowListenerContext context);

    long slowerThanMills();

    void setSlowQueryMills(final long timeMills);
}
