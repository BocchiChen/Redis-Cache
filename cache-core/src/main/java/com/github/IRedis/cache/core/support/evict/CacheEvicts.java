package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICacheEvict;

public final class CacheEvicts {
    private CacheEvicts(){}

    public static <K, V> ICacheEvict<K,V> none(){
        return new CacheEvictNone<>();
    }

    public static <K, V> ICacheEvict<K,V> fifo(){
        return new CacheEvictFifo<>();
    }

    public static <K, V> ICacheEvict<K,V> lru(){
        return new CacheEvictLru<>();
    }

    public static <K, V> ICacheEvict<K,V> lruDoubleListMap(){
        return new CacheEvictLruDoubleListMap<>();
    }

    public static <K, V> ICacheEvict<K,V> lruLinkedHashMap(){
        return new CacheEvictLruLinkedHashMap<>();
    }

    public static <K, V> ICacheEvict<K,V> lfu(){
        return new CacheEvictLfu<>();
    }
}
