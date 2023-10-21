package com.github.IRedis.cache.core.core;

import com.github.IRedis.cache.api.*;
import com.github.IRedis.cache.api.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Cache<K, V> implements ICache<K, V> {

    private Map<K, V> map;

    private int sizeLimit;

    private ICacheEvict<K, V> evict;

    private ICacheExpire<K, V> expire;

    private ICachePersist<K, V> persist;

    private List<ICacheRemoveListener<K, V>> removeListeners;

    private List<ICacheSlowListener> slowListeners;

    public Cache<K, V> map(Map<K, V> map){
        this.map = map;
        return this;
    }

    public Cache<K, V> sizeLimit(int sizeLimit){
        this.sizeLimit = sizeLimit;
        return this;
    }

    public Cache<K, V> evict(ICacheEvict<K, V> evict){
        this.evict = evict;
        return this;
    }

    public Cache<K, V> removeListeners(List<ICacheRemoveListener<K, V>> removeListeners){
        this.removeListeners = removeListeners;
        return this;
    }

    public Cache<K, V> slowListeners(List<ICacheSlowListener> slowListeners){
        this.slowListeners = slowListeners;
        return this;
    }

    @Override
    public ICache<K, V> expire(K key, long timeInMills) {
        return null;
    }

    @Override
    public ICache<K, V> expireAt(K key, long timeInMills) {
        return null;
    }

    @Override
    public List<ICacheRemoveListener<K, V>> removeListeners() {
        return this.removeListeners;
    }

    @Override
    public List<ICacheSlowListener> slowListeners() {
        return this.slowListeners;
    }

    @Override
    public ICacheEvict<K, V> evict() {
        return null;
    }

    @Override
    public ICachePersist<K, V> persist() {
        return this.persist;
    }

    @Override
    public ICacheExpire<K, V> expire() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return null;
    }

    @Override
    public V put(K key, V value) {
        return null;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear() {

    }

    @Override
    public Set<K> keySet() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return null;
    }
}
