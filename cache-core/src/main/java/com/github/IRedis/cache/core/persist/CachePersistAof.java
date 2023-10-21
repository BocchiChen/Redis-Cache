package com.github.IRedis.cache.core.persist;

import com.github.IRedis.cache.api.ICache;
import com.github.support.heaven.lang.StringUtil;
import com.github.support.heaven.util.io.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CachePersistAof<K, V> extends CachePersistAdaptor<K, V>{

    private static final Logger log = LoggerFactory.getLogger(CachePersistAof.class);

    private final List<String> bufferList = new ArrayList<>();

    private final String dbPath;

    public CachePersistAof(String dbPath){
        this.dbPath = dbPath;
    }

    @Override
    public void persist(ICache<K, V> cache) {
        log.info("开始AOF持久化到文件");
        if(!FileUtil.exists(this.dbPath)){
            FileUtil.createFile(this.dbPath);
        }
        FileUtil.write(this.dbPath, bufferList);
        bufferList.clear();
        log.info("完成AOF持久化到文件");
    }

    @Override
    public long delay() {
        return 1;
    }

    @Override
    public long period() {
        return 1;
    }

    @Override
    public TimeUnit timeUnit() {
        return TimeUnit.SECONDS;
    }

    public void append(final String json){
        if(!StringUtil.isEmpty(json)){
            this.bufferList.add(json);
        }
    }
}
