package com.github.IRedis.cache.core.model;

import com.github.IRedis.cache.api.ICacheEntry;

public class CacheEntry<K, V> implements ICacheEntry<K, V> {

    private final K key;

    private final V value;

    public CacheEntry(K key, V value){
        this.key = key;
        this.value = value;
    }


    public static <K, V> CacheEntry<K, V> of(final K key, final V value){
        return new CacheEntry<>(key, value);
    }

    @Override
    public K key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }


}
