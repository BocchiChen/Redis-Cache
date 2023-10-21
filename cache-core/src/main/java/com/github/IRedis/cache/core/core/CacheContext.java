package com.github.IRedis.cache.core.core;

import com.github.IRedis.cache.api.ICacheContext;
import com.github.IRedis.cache.api.ICacheEvict;

import java.util.Map;

public class CacheContext<K, V> implements ICacheContext<K, V> {

    private Map<K, V> map;

    private int size;

    private ICacheEvict<K, V> evict;

    @Override
    public Map<K, V> map() {
        return this.map;
    }

    public CacheContext<K, V> map(Map<K, V> map){
        this.map = map;
        return this;
    }

    @Override
    public int size() {
        return this.size;
    }

    public CacheContext<K, V> size(int size){
        this.size = size;
        return this;
    }

    @Override
    public ICacheEvict<K, V> cacheEvict() {
        return this.evict;
    }

    public CacheContext<K, V> cacheEvict(ICacheEvict<K, V> evict){
        this.evict = evict;
        return this;
    }
}
