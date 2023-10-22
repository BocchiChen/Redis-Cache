package com.github.IRedis.cache.core.persist;

import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.core.model.PersistRdbEntry;
import com.github.support.heaven.util.io.FileUtil;
import com.google.gson.Gson;

import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class CachePersistRdb<K, V> extends CachePersistAdaptor<K, V>{

    private final Gson gson = new Gson();

    private final String filePath;

    public CachePersistRdb(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void persist(ICache<K, V> cache) {
        Set<Map.Entry<K, V>> set = cache.entrySet();
        FileUtil.createFile(filePath);
        FileUtil.truncate(filePath);
        for(Map.Entry<K, V> entry : set){
            K key = entry.getKey();
            Long expireTime = cache.expire().expireTime(key);
            PersistRdbEntry<K, V> persistRdbEntry = new PersistRdbEntry<>();
            persistRdbEntry.setKey(key);
            persistRdbEntry.setValue(entry.getValue());
            persistRdbEntry.setExpire(expireTime);
            String line = gson.toJson(persistRdbEntry);
            FileUtil.write(filePath, line, StandardOpenOption.APPEND);
        }
    }

    @Override
    public long delay() {
        return 5;
    }

    @Override
    public long period() {
        return 5;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.MINUTES;
    }
}
