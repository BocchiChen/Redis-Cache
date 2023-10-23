package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.model.CacheEntry;
import com.github.IRedis.cache.core.support.struct.lru.ILruMap;
import com.github.IRedis.cache.core.support.struct.lru.impl.LruMapDoubleList;

import java.util.HashMap;

public class CacheEvictLru2<K, V> extends AbstractCacheEvict<K, V> {

    private final ILruMap<K, V> firstLruMap;

    private final ILruMap<K, V> moreLruMap;

    public CacheEvictLru2(){
        this.firstLruMap = new LruMapDoubleList<>();
        this.moreLruMap = new LruMapDoubleList<>();
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> result = null;
        ICache<K, V> cache = context.cache();
        if(context.size() >= cache.size()){
            ICacheEntry<K, V> evictEntry = null;
            if(!firstLruMap.isEmpty()){
                evictEntry = firstLruMap.removeEldest();
            }else{
                evictEntry = moreLruMap.removeEldest();
            }
            final K evictKey = evictEntry.key();
            V evictValue = cache.remove(evictKey);
            result = new CacheEntry<>(evictKey, evictValue);
        }
        return result;
    }

    @Override
    public void updateKey(K key) {
        if(moreLruMap.contains(key) || firstLruMap.contains(key)){
            this.removeKey(key);
            moreLruMap.updateKey(key);
        } else {
            firstLruMap.updateKey(key);
        }
    }

    @Override
    public void removeKey(K key) {
        if(moreLruMap.contains(key)){
            moreLruMap.removeKey(key);
        }else{
            firstLruMap.removeKey(key);
        }
    }

}
