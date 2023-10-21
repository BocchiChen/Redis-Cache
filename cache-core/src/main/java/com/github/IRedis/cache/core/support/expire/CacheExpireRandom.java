package com.github.IRedis.cache.core.support.expire;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheExpire;
import com.github.IRedis.cache.api.ICacheRemoveListener;
import com.github.IRedis.cache.api.ICacheRemoveListenerContext;
import com.github.IRedis.cache.core.constant.enums.CacheRemoveType;
import com.github.IRedis.cache.core.exception.CacheRuntimeException;
import com.github.IRedis.cache.core.listener.remove.CacheRemoveListenerContext;
import com.github.IRedis.cache.core.support.evict.CacheEvictLfu;
import com.github.IRedis.cache.core.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class CacheExpireRandom<K, V> implements ICacheExpire<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheEvictLfu.class);

    private static final int LIMIT = 100;

    private final Map<K, Long> expireMap = new HashMap<>();

    private final ICache<K, V> cache;

    private volatile boolean fastmode = false;

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    public CacheExpireRandom(ICache<K, V> cache){
        this.cache = cache;
        init();
    }

    private void init(){
        EXECUTOR_SERVICE.scheduleAtFixedRate(new ExpireThreadRandom(), 100, 100, TimeUnit.MILLISECONDS);
    }

    private class ExpireThreadRandom implements Runnable{
        @Override
        public void run() {
            if(expireMap.isEmpty()){
                return;
            }
            if(fastmode){
                expireKeys(10L);
            }else{
                expireKeys(100L);
            }
        }
    }

    private void expireKeys(final long timeoutMillis){
        final long timeLimit = System.currentTimeMillis() + timeoutMillis;
        this.fastmode = false;
        int count = 0;
        while(true){
            if (count >= LIMIT) {
                log.info("过期淘汰次数已经达到最大次数: {}，完成本次执行。", LIMIT);
                return;
            }
            if (System.currentTimeMillis() >= timeLimit) {
                this.fastmode = true;
                log.info("过期淘汰已经达到限制时间，中断本次执行，设置 fastMode=true;");
                return;
            }
            K key = this.getRandomKey();
            long expireTime = this.expireMap.get(key);
            boolean removeFlag = this.expireKey(key, expireTime);
            log.debug("key: {} 过期执行结果 {}", key, removeFlag);
            count++;
        }
    }

    private K getRandomKey(){
        Random random = ThreadLocalRandom.current();
        Set<K> keyset = this.expireMap.keySet();
        List<K> keylist = new ArrayList<>(keyset);
        int randomIndex = random.nextInt(keylist.size());
        return keylist.get(randomIndex);
    }

    private K getRandomKey2(){
        Random random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(this.expireMap.size());
        int count = 0;
        for (K key : this.expireMap.keySet()) {
            if (randomIndex == count) {
                return key;
            }
            count++;
        }
        throw new CacheRuntimeException("对应信息不存在");
    }

    private Set<K> getRandomKeyBatch(final int sizeLimit){
        Random random = ThreadLocalRandom.current();
        int randomIndex = random.nextInt(this.expireMap.size());
        int count = 0;
        Set<K> keySet = new HashSet<>();
        for (K key : this.expireMap.keySet()) {
            if (keySet.size() >= sizeLimit) {
                return keySet;
            }
            if (count >= randomIndex) {
                keySet.add(key);
            }
            count++;
        }
        throw new CacheRuntimeException("对应信息不存在");
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

    private boolean expireKey(final K key, final Long expireAt){
        if(expireAt == null){
            return false;
        }
        // 永不过期
        if(expireAt == NONE_EXPIRE){
            return false;
        }
        long currentTime = System.currentTimeMillis();
        if (currentTime >= expireAt) {
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

            return true;
        }
        return false;
    }
}
