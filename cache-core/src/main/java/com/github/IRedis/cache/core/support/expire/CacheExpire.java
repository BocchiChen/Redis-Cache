package com.github.IRedis.cache.core.support.expire;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheExpire;
import com.github.IRedis.cache.api.ICacheRemoveListener;
import com.github.IRedis.cache.api.ICacheRemoveListenerContext;
import com.github.IRedis.cache.core.constant.enums.CacheRemoveType;
import com.github.IRedis.cache.core.util.CollectionUtil;
import com.github.IRedis.cache.api.*;
import com.github.IRedis.cache.core.listener.remove.CacheRemoveListenerContext;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheExpire<K, V> implements ICacheExpire<K, V> {
    private static final int LIMIT = 100;

    private final Map<K, Long> expireMap = new HashMap<>();

    private final ICache<K, V> cache;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpire(ICache<K, V> cache){
        this.cache = cache;
        this.init();
    }

    private void init(){
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThread(), 100, 100, TimeUnit.MILLISECONDS);
    }

    @Override
    public void expire(K key, long expireAt) {
        this.expireMap.put(key, expireAt);
    }

    @Override
    public void refreshExpire(Collection<K> keyList) {
        if(CollectionUtil.isEmpty(keyList)){
            return;
        }
        if(keyList.size() <= this.expireMap.size()){
            for(K key : keyList){
                Long expireValue = this.expireMap.get(key);
                this.expireKey(key, expireValue);
            }
        }else{
            for(Map.Entry<K, Long> entry : this.expireMap.entrySet()){
                this.expireKey(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public Long expireTime(K key) {
        return this.expireMap.get(key);
    }

    private class ExpireThread implements Runnable{
        @Override
        public void run() {
            if(expireMap.isEmpty()){
                return;
            }
            int count = 0;
            for(Map.Entry<K, Long> entry : expireMap.entrySet()){
                if(count >= LIMIT){
                    return;
                }
                expireKey(entry.getKey(), entry.getValue());
                count++;
            }
        }
    }

    private void expireKey(final K key, final Long expireAt){
        if(expireAt == null){
            return;
        }
        // 永不过期
        if(expireAt == NONE_EXPIRE){
            return;
        }
        long currentTime = System.currentTimeMillis();
        if(currentTime >= expireAt){
            this.expireMap.remove(key);
            V expireValue = this.cache.remove(key);

            // 删除淘汰监听器
            ICacheRemoveListenerContext<K, V> context = CacheRemoveListenerContext.<K, V>newInstance()
                    .key(key)
                    .value(expireValue)
                    .type(CacheRemoveType.EXPIRE.code());

            for(ICacheRemoveListener<K, V> listener : cache.removeListeners()){
                listener.listen(context);
            }

        }
    }

}
