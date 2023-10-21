package com.github.IRedis.cache.core.support.evict;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheEntry;
import com.github.IRedis.cache.api.ICacheEvictContext;
import com.github.IRedis.cache.core.model.CacheEntry;
import com.github.IRedis.cache.core.util.CollectionUtil;
import com.github.IRedis.cache.core.util.ObjectUtil;
import com.github.IRedis.cache.core.exception.CacheRuntimeException;
import com.github.IRedis.cache.core.model.FreqNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class CacheEvictLfu<K, V> extends AbstractCacheEvict<K, V>{

    private static final Logger log = LoggerFactory.getLogger(CacheEvictLfu.class);

    private final Map<K, FreqNode<K, V>> keyMap;

    private final Map<Long, LinkedHashSet<FreqNode<K, V>>> freqMap;

    private long minFreq;

    public CacheEvictLfu() {
        this.keyMap = new HashMap<>();
        this.freqMap = new HashMap<>();
        this.minFreq = 1;
    }

    @Override
    protected ICacheEntry<K, V> doEvict(ICacheEvictContext<K, V> context) {
        ICacheEntry<K, V> cacheEntry = null;
        ICache<K, V> cache = context.cache();

        if(cache.size() >= context.size()){
            FreqNode<K, V> freqNode = this.getMinFreqNode();
            K evictKey = freqNode.getKey();
            V evictValue = cache.remove(evictKey);
            this.removeKey(evictKey);
            log.debug("淘汰最小频率信息, key: {}, value: {}, freq: {}",
                    evictKey, evictValue, freqNode.getFrequency());
            cacheEntry = new CacheEntry<>(evictKey, evictValue);
        }

        return cacheEntry;
    }

    @Override
    public void updateKey(K key){
        FreqNode<K, V> freqNode = this.keyMap.get(key);
        if(!ObjectUtil.isNull(freqNode)){
            long freq = freqNode.getFrequency();
            LinkedHashSet<FreqNode<K, V>> set = this.freqMap.get(freq);
            set.remove(freqNode);
            if (this.minFreq == freq && set.isEmpty()) {
                this.minFreq++;
            }
            freq++;
            freqNode.frequency(freq);
            this.addToFreqMap(freq, freqNode);
        }else{
            freqNode = new FreqNode<>(key, null);
            this.keyMap.put(key, freqNode);
            this.addToFreqMap(1, freqNode);
            this.minFreq = 1;
        }
    }

    @Override
    public void removeKey(K key){
        FreqNode<K, V> freqNode = this.keyMap.remove(key);
        if (ObjectUtil.isNull(freqNode)) {
            return;
        }
        long freq = freqNode.getFrequency();
        LinkedHashSet<FreqNode<K, V>> set = this.freqMap.get(freq);
        set.remove(freqNode);
        if(CollectionUtil.isEmpty(set) && freq == this.minFreq){
            this.updateMinFreq();
        }
    }

    private FreqNode<K, V> getMinFreqNode(){
        LinkedHashSet<FreqNode<K, V>> minFreqSet = freqMap.get(this.minFreq);

        if(CollectionUtil.isNotEmpty(minFreqSet)){
            return minFreqSet.iterator().next();
        }

        throw new CacheRuntimeException("找不到最小频率的key");
    }

    private void addToFreqMap(final long freq, FreqNode<K, V> freqNode){
        LinkedHashSet<FreqNode<K, V>> set = this.freqMap.get(freq);
        if(set == null){
            this.freqMap.put(freq, new LinkedHashSet<>());
        }
        assert set != null;
        set.add(freqNode);
        this.freqMap.put(freq, set);
    }

    private void updateMinFreq(){
        for(Map.Entry<K, FreqNode<K, V>> entry : this.keyMap.entrySet()){
            this.minFreq = Math.min(this.minFreq, entry.getValue().getFrequency());
        }
    }
}
