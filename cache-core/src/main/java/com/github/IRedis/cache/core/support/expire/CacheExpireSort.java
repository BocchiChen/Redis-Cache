package com.github.IRedis.cache.core.support.expire;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheExpire;
import com.github.IRedis.cache.core.util.CollectionUtil;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheExpireSort<K, V> implements ICacheExpire<K, V> {

    private static final int LIMIT = 100;

    private final Map<Long, List<K>> sortMap = new TreeMap<>(new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return (int)(o1 - o2);
        }
    });

    private final Map<K, Long> expireMap = new HashMap<>();

    private final ICache<K, V> cache;

    private final static ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireSort(ICache<K, V> cache){
        this.cache = cache;
        this.init();
    }

    private void init(){
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThreadSort(), 100, 100, TimeUnit.MILLISECONDS);
    }

    private class ExpireThreadSort implements Runnable{
        @Override
        public void run() {
            if(sortMap.isEmpty()){
                return;
            }
            int count = 0;
            for(Map.Entry<Long, List<K>> entry : sortMap.entrySet()){
                if(count >= LIMIT){
                    return;
                }
                Long expireTime = entry.getKey();
                List<K> keylist = entry.getValue();
                if(CollectionUtil.isEmpty(keylist)){
                    sortMap.remove(expireTime);
                    continue;
                }
                if(System.currentTimeMillis() >= expireTime){
                    Iterator<K> iterator = keylist.iterator();
                    while(iterator.hasNext()){
                        K key = iterator.next();
                        iterator.remove();
                        expireMap.remove(key);

                        cache.remove(key);
                    }
                }else{
                    return;
                }
                count++;
            }
        }
    }

    private void removeExpireKey(final K key){
        Long expireTime = this.expireMap.get(key);
        if(expireTime == null){
            return;
        }
        // 永不过期
        if(expireTime == NONE_EXPIRE){
            return;
        }
        final long currentTime = System.currentTimeMillis();
        if(currentTime >= expireTime){
            List<K> keylist = this.sortMap.get(expireTime);
            if(CollectionUtil.isEmpty(keylist)){
                return;
            }
            keylist.remove(key);
            this.sortMap.put(expireTime, keylist);
            this.expireMap.remove(key);
        }
    }

    @Override
    public void expire(K key, long expireAt) {
        List<K> keylist = this.sortMap.get(expireAt);
        if(CollectionUtil.isEmpty(keylist)){
            keylist = new ArrayList<>();
        }
        keylist.add(key);
        this.sortMap.put(expireAt, keylist);
        this.expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if(CollectionUtil.isEmpty(keyList)) {
            return;
        }
        // 这样维护两套的代价太大，后续优化，暂时不用。
        // 判断大小，小的作为外循环
        final int expireSize = this.expireMap.size();
        if(expireSize <= keyList.size()) {
            // 一般过期的数量都是较少的
            for(Map.Entry<K,Long> entry : this.expireMap.entrySet()) {
                K key = entry.getKey();
                // 这里直接执行过期处理，不再判断是否存在于集合中。
                // 因为基于集合的判断，时间复杂度为 O(n)
                this.removeExpireKey(key);
            }
        } else {
            for(K key : keyList) {
                this.removeExpireKey(key);
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return this.expireMap.get(key);
    }
}
