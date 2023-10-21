package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEvictContext;

public class CacheEvictContext<K, V> implements ICacheEvictContext<K, V> {

    private K key;

    private ICache<K, V> cache;

    private int size;

    @Override
    public K key() {
        return this.key;
    }

    @Override
    public ICache<K, V> cache() {
        return this.cache;
    }

    @Override
    public int size() {
        return this.size;
    }

    public CacheEvictContext<K, V> key(K key){
        this.key = key;
        return this;
    }

    public CacheEvictContext<K, V> cache(ICache<K, V> cache){
        this.cache = cache;
        return this;
    }

    public CacheEvictContext<K, V> size(int size){
        this.size = size;
        return this;
    }
}
