package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.model.CacheEntry;

import java.util.LinkedList;
import java.util.Queue;

public class CacheEvictFifo<K, V> extends AbstractCacheEvict<K, V>{

    private final Queue<K> queue = new LinkedList<>();

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEvictEntry = null;
        final ICache<K, V> cache = context.cache();
        if(cache.size() >= context.size()){
            K evictKey = queue.remove();
            V evictValue = cache.remove(evictKey);
            cacheEvictEntry = new CacheEntry<>(evictKey, evictValue);
        }

        return cacheEvictEntry;
    }

    @Override
    public void updateKey(final K key) {
        this.removeKey(key);
        this.queue.add(key);
    }

    @Override
    public void removeKey(final K key) {
        this.queue.remove(key);
    }

}
