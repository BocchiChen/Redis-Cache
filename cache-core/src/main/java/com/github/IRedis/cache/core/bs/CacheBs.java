package com.github.IRedis.cache.core.bs;

import com.github.IRedis.cache.api.ICacheEvict;
import com.github.IRedis.cache.core.support.evict.CacheEvicts;
import com.github.IRedis.cache.core.util.ArgUtil;
import com.github.IRedis.cache.api.*;

import java.util.HashMap;
import java.util.Map;

public class CacheBs<K, V> {
    private CacheBs(){}

    public static <K, V> CacheBs<K, V> newInstance(){
        return new CacheBs<>();
    }

    /*
     * 缓存关联map
     */
    private Map<K, V> map = new HashMap<>();

    /*
    * 缓存大小
     */
    private int size = Integer.MAX_VALUE;

    private ICacheEvict<K, V> cacheEvict = CacheEvicts.fifo();


    CacheBs<K, V> map(Map<K, V> map){
        ArgUtil.notNull(map, "CacheMap");
        this.map = map;
        return this;
    }

    CacheBs<K, V> size(int size){
        ArgUtil.notNegative(size, "CacheSize");
        this.size = size;
        return this;
    }



}
