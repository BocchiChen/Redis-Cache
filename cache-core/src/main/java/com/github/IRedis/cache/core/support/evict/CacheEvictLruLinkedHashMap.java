package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.api.ICacheEvict;
import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.model.CacheEntry;

import java.util.LinkedHashMap;
import java.util.Map;

public class CacheEvictLruLinkedHashMap<K, V> extends LinkedHashMap<K, V> implements ICacheEvict<K, V> {

    private volatile boolean removeFlag = false;

    private transient Map.Entry<K, V> eldestEntry = null;

    public CacheEvictLruLinkedHashMap(){
        super(16, 0.75f, true);
    }

    @Override
    public ICacheEntry<K, V> evict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEntry = null;
        ICache<K, V> cache = context.cache();
        if (cache.size() >= context.size()) {
            removeFlag = true;
            super.put(context.key(), null);

            K evictKey = eldestEntry.getKey();
            V evictValue = eldestEntry.getValue();
            cacheEntry = new CacheEntry<>(evictKey, evictValue);
        } else {
            removeFlag = false;
        }

        return cacheEntry;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest){
        this.eldestEntry = eldest;
        return removeFlag;
    }

    @Override
    public void updateKey(K key) {
        super.put(key, null);
    }

    @Override
    public void removeKey(K key) {
        super.remove(key);
    }
}
