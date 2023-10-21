package com.github.IRedis.cache.core.support.struct.lru.impl;

import com.github.IRedis.cache.core.model.CircleListNode;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.core.exception.CacheRuntimeException;
import com.github.IRedis.cache.core.model.CacheEntry;
import com.github.IRedis.cache.core.support.struct.lru.ILruMap;
import com.github.IRedis.cache.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

public class LruMapCircleList<K, V> implements ILruMap<K, V> {

    private CircleListNode<K, V> head;

    private Map<K, CircleListNode<K, V>> indexMap;

    private CircleListNode<K, V> pointer;

    public LruMapCircleList(){
        this.head = new CircleListNode<>();
        this.head.next(head);
        this.head.prev(head);
        this.indexMap = new HashMap<>();
        this.pointer = head;
    }

    @Override
    public ICacheEntry<K, V> removeEldest() {
        // fast-fail
        if(isEmpty()){
            throw new CacheRuntimeException("不可删除头结点");
        }
        if (pointer == head) {
            pointer = head.getNext();
        }
        while(pointer != head){
            if (!pointer.getAccessFlag()) {
                K evictKey = pointer.getKey();
                V evictValue = pointer.getValue();
                pointer = pointer.getNext();
                this.removeKey(evictKey);
                return CacheEntry.of(evictKey, evictValue);
            }
            pointer.accessFlag(false);
            pointer = pointer.getNext();
        }
        CircleListNode<K, V> eldestNode = this.head.getNext();
        if (pointer == eldestNode) {
            pointer = eldestNode.getNext();
        }
        this.removeKey(eldestNode.getKey());
        return CacheEntry.of(eldestNode.getKey(), eldestNode.getValue());
    }

    @Override
    public void updateKey(final K key) {
        CircleListNode<K, V> node = this.indexMap.get(key);
        if(!ObjectUtil.isNull(node)){
            node.accessFlag(true);
            return;
        }
        node = new CircleListNode<>(key);
        // 加入到末尾
        CircleListNode<K, V> lastNode = this.head.getPrev();
        node.prev(lastNode);
        node.next(this.head);
        lastNode.next(node);
        this.head.prev(node);
        this.indexMap.put(key, node);
    }

    @Override
    public void removeKey(final K key) {
        CircleListNode<K, V> node = indexMap.get(key);
        if (ObjectUtil.isNull(node)) {
            return;
        }
        CircleListNode<K, V> prev = node.getPrev();
        CircleListNode<K, V> next = node.getNext();

        if (pointer == node) {
            pointer = next;
        }

        prev.next(next);
        next.prev(prev);

        this.indexMap.remove(key);
    }

    @Override
    public boolean isEmpty() {
        return indexMap.isEmpty();
    }

    @Override
    public boolean contains(final K key) {
        return indexMap.containsKey(key);
    }
}
