package com.github.IRedis.cache.core.interceptor;

import com.github.IRedis.cache.api.ICacheInterceptor;
import com.github.IRedis.cache.core.interceptor.aof.CacheInterceptorAof;
import com.github.IRedis.cache.core.interceptor.common.CacheInterceptorCost;
import com.github.IRedis.cache.core.interceptor.evict.CacheInterceptorEvict;
import com.github.IRedis.cache.core.interceptor.refresh.CacheInterceptorRefresh;

import java.util.ArrayList;
import java.util.List;

public class CacheInterceptors {
    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defautCommonList(){
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorCost());
        return list;
    }

    @SuppressWarnings("all")
    public static ICacheInterceptor aof(){
        return new CacheInterceptorAof();
    }

    @SuppressWarnings("all")
    public static ICacheInterceptor evict(){
        return new CacheInterceptorEvict();
    }

    @SuppressWarnings("all")
    public static List<ICacheInterceptor> defautRefreshList(){
        List<ICacheInterceptor> list = new ArrayList<>();
        list.add(new CacheInterceptorRefresh());
        return list;
    }
}
