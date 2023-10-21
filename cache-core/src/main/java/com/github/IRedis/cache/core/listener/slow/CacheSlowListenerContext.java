package com.github.IRedis.cache.core.listener.slow;

import com.github.IRedis.cache.api.ICacheSlowListenerContext;

public class CacheSlowListenerContext implements ICacheSlowListenerContext {

    private String methodName;

    private Object[] params;

    private Object result;

    private long startTimeMills;

    private long endTimeMills;

    private long costTimeMills;

    public static CacheSlowListenerContext newInstance(){
        return new CacheSlowListenerContext();
    }

    @Override
    public String methodName() {
        return this.methodName;
    }

    public CacheSlowListenerContext methodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    @Override
    public Object[] params() {
        return this.params;
    }

    public CacheSlowListenerContext params(Object[] params) {
        this.params = params;
        return this;
    }

    @Override
    public Object result() {
        return this.result;
    }

    public CacheSlowListenerContext result(Object result) {
        this.result = result;
        return this;
    }

    @Override
    public long startTimeMills() {
        return this.startTimeMills;
    }

    public CacheSlowListenerContext startTimeMills(long startTimeMills) {
        this.startTimeMills = startTimeMills;
        return this;
    }

    @Override
    public long endTimeMills() {
        return this.endTimeMills;
    }

    public CacheSlowListenerContext endTimeMills(long endTimeMills) {
        this.endTimeMills = endTimeMills;
        return this;
    }

    @Override
    public long costTimeMills() {
        return this.costTimeMills;
    }

    public CacheSlowListenerContext costTimeMills(long costTimeMills) {
        this.costTimeMills = costTimeMills;
        return this;
    }
}
