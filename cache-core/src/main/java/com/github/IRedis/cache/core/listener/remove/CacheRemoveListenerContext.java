package com.github.IRedis.cache.core.listener.remove;

import com.github.IRedis.cache.api.ICacheRemoveListenerContext;

public class CacheRemoveListenerContext<K, V> implements ICacheRemoveListenerContext<K, V> {

    private K key;

    private V value;

    private String type;

    public static <K, V> CacheRemoveListenerContext<K, V> newInstance() {
        return new CacheRemoveListenerContext<>();
    }

    public CacheRemoveListenerContext<K, V> key(K key){
        this.key = key;
        return this;
    }

    public CacheRemoveListenerContext<K, V> value(V value){
        this.value = value;
        return this;
    }

    public CacheRemoveListenerContext<K, V> type(String type){
        this.type = type;
        return this;
    }

    @Override
    public K key() {
        return this.key;
    }

    @Override
    public V value() {
        return this.value;
    }

    @Override
    public String type() {
        return this.type;
    }
}
