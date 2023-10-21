package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.List;

public class CacheEvictLru<K, V> extends AbstractCacheEvict<K, V> {

    private final List<K> list = new LinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEntry = null;
        ICache<K, V> cache = context.cache();
        if(cache.size() >= context.size()) {
            K evictKey = this.list.remove(this.list.size() - 1);
            V evictValue = cache.remove(evictKey);
            cacheEntry = new CacheEntry<>(evictKey, evictValue);
        }
        return cacheEntry;
    }

    @Override
    public void updateKey(K key){
        this.list.remove(key);
        this.list.add(0, key);
    }

    @Override
    public void removeKey(K key){
        this.list.remove(key);
    }
}
