package com.github.IRedis.cache.core.load;

import com.github.IRedis.cache.api.ICacheLoad;

public class CacheLoads {
    private CacheLoads(){}

    public static <K,V> ICacheLoad<K,V> none() {
        return new CacheLoadNone<>();
    }

    public static <K,V> ICacheLoad<K,V> rdb(final String dbPath) {
        return new CacheLoadRdb<>(dbPath);
    }

    public static <K,V> ICacheLoad<K,V> aof(final String dbPath) {
        return new CacheLoadAof<>(dbPath);
    }
}
