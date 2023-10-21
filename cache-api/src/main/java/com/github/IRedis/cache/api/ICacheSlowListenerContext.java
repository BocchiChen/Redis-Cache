package com.github.IRedis.cache.api;

public interface ICacheSlowListenerContext {
    String methodName();

    Object[] params();

    Object result();

    long startTimeMills();

    long endTimeMills();

    long costTimeMills();
}
