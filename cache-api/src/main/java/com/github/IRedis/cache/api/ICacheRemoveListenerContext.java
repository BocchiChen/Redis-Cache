package com.github.IRedis.cache.api;

public interface ICacheRemoveListenerContext<K, V> {
    K key();

    V value();

    String type();
}
