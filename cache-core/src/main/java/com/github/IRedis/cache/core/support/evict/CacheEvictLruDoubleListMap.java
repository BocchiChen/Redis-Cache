package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.exception.CacheRuntimeException;
import com.github.IRedis.cache.core.model.CacheEntry;
import com.github.IRedis.cache.core.model.DoubleListNode;
import com.github.IRedis.cache.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

public class CacheEvictLruDoubleListMap<K, V> extends AbstractCacheEvict<K, V> {

    private final DoubleListNode<K, V> head;

    private final DoubleListNode<K, V> tail;

    private final Map<K, DoubleListNode<K, V>> indexMap;

    public CacheEvictLruDoubleListMap(){
        this.head = new DoubleListNode<>();
        this.tail = new DoubleListNode<>();
        this.head.next(tail);
        this.tail.prev(head);
        this.indexMap = new HashMap<>();
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEntry = null;
        ICache<K, V> cache = context.cache();

        if(cache.size() >= context.size()){
            DoubleListNode<K, V> eldest = this.tail.getPrev();
            if (eldest == this.head) {
                throw new CacheRuntimeException("不可删除头结点!");
            }

            K evictKey = eldest.getKey();
            V evictValue = cache.remove(evictKey);

            this.removeKey(evictKey);

            cacheEntry = new CacheEntry<>(evictKey, evictValue);
        }

        return cacheEntry;
    }

    @Override
    public void updateKey(K key){
        this.removeKey(key);

        DoubleListNode<K, V> addedNode = new DoubleListNode<>(key, null);
        DoubleListNode<K, V> firstNode = this.head.getNext();

        this.head.next(addedNode);
        addedNode.prev(this.head);

        addedNode.next(firstNode);
        firstNode.prev(addedNode);

        this.indexMap.put(key, addedNode);
    }

    @Override
    public void removeKey(K key){
        DoubleListNode<K, V> removeNode = this.indexMap.get(key);
        if (ObjectUtil.isNull(removeNode)) {
            return;
        }
        DoubleListNode<K, V> prev = removeNode.getPrev();
        DoubleListNode<K, V> next = removeNode.getNext();

        prev.next(next);
        next.prev(prev);

        this.indexMap.remove(key);
    }
}
