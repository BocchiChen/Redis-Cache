package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.support.struct.lru.impl.LruMapCircleList;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.core.model.CacheEntry;
import com.github.IRedis.cache.core.support.struct.lru.ILruMap;

public class CacheEvictClock<K, V> extends AbstractCacheEvict<K, V> {

    private final ILruMap<K, V> circleLinkedList;

    public CacheEvictClock(){
        this.circleLinkedList = new LruMapCircleList<>();
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEntry = null;
        ICache<K, V> cache = context.cache();
        if(cache.size() >= context.size()){
            ICacheEntry<K, V> evictEntry = this.circleLinkedList.removeEldest();
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);
            cacheEntry = new CacheEntry<>(evictKey, evictValue);
        }
        return cacheEntry;
    }

    @Override
    public void updateKey(final K key) {
        this.circleLinkedList.updateKey(key);
    }

    @Override
    public void removeKey(final K key) {
        this.circleLinkedList.removeKey(key);
    }
}
