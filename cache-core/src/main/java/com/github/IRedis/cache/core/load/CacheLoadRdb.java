package com.github.IRedis.cache.core.load;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheLoad;
import com.github.IRedis.cache.core.model.PersistRdbEntry;
import com.github.IRedis.cache.core.util.CollectionUtil;
import com.github.IRedis.cache.core.util.ObjectUtil;
import com.github.support.heaven.lang.StringUtil;
import com.github.support.heaven.util.io.FileUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CacheLoadRdb<K, V> implements ICacheLoad<K, V> {

    private final Gson gson = new Gson();

    private static final Logger log = LoggerFactory.getLogger(CacheLoadRdb.class);

    private final String dbPath;

    public CacheLoadRdb(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void load(ICache<K, V> cache) {
        List<String> lines = FileUtil.readAllLines(dbPath);
        log.info("[load] 开始处理 path: {}", dbPath);
        if(CollectionUtil.isEmpty(lines)) {
            log.info("[load] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }

        for(String line : lines) {
            if(StringUtil.isEmpty(line)) {
                continue;
            }

            PersistRdbEntry<K,V> entry = gson.fromJson(line, PersistRdbEntry.class);

            K key = entry.getKey();
            V value = entry.getValue();
            Long expire = entry.getExpire();

            cache.put(key, value);
            if(!ObjectUtil.isNull(expire)) {
                cache.expireAt(key, expire);
            }
            // 需要加策略和过期索引
        }
    }
}
