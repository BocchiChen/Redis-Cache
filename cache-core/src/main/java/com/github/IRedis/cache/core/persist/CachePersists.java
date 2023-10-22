package com.github.IRedis.cache.core.persist;

import com.github.IRedis.cache.api.ICachePersist;

public class CachePersists {
    private CachePersists(){}

    public static <K, V> ICachePersist<K, V> none(){
        return new CachePersistNone<>();
    }

    public static <K, V> ICachePersist<K, V> aof(final String path){
        return new CachePersistAof<>(path);
    }

    public static <K, V> ICachePersist<K, V> rdb(final String path){
        return new CachePersistRdb<>(path);
    }
}
