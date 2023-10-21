package com.github.IRedis.cache.core.support.struct.lru.impl;

import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.core.exception.CacheRuntimeException;
import com.github.IRedis.cache.core.model.CacheEntry;
import com.github.IRedis.cache.core.model.DoubleListNode;
import com.github.IRedis.cache.core.support.struct.lru.ILruMap;
import com.github.IRedis.cache.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

public class LruMapDoubleList<K, V> implements ILruMap<K, V> {

    private final DoubleListNode<K, V> head;

    private final DoubleListNode<K, V> tail;

    private final Map<K, DoubleListNode<K, V>> indexMap;

    public LruMapDoubleList() {
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();
        this.indexMap = new HashMap<>();
        this.head.next(this.tail);
        this.tail.prev(this.head);
    }

    @Override
    public ICacheEntry<K, V> removeEldest() {
        DoubleListNode<K, V> eldest = this.tail.getPrev();
        if(eldest == this.head){
            throw new CacheRuntimeException("不可删除头结点");
        }
        K evictKey = eldest.getKey();
        V evictValue = eldest.getValue();
        this.removeKey(evictKey);
        return CacheEntry.of(evictKey, evictValue);
    }

    @Override
    public void updateKey(final K key) {
        this.removeKey(key);
        DoubleListNode<K, V> newNode = new DoubleListNode<>(key, null);
        DoubleListNode<K, V> secondNode = this.head.getNext();
        this.head.next(newNode);
        newNode.prev(this.head);
        secondNode.prev(newNode);
        newNode.next(secondNode);
        this.indexMap.put(key, newNode);
    }

    @Override
    public void removeKey(final K key) {
        DoubleListNode<K, V> node = this.indexMap.get(key);
        if(ObjectUtil.isNull(node)){
            return;
        }
        DoubleListNode<K, V> prev = node.getPrev();
        DoubleListNode<K, V> next = node.getNext();
        prev.prev(next);
        next.prev(prev);
        this.indexMap.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return this.indexMap.isEmpty();
    }

    @Override
    public boolean contains(K key) {
        return this.indexMap.containsKey(key);
    }
}
