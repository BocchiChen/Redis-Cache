package com.github.IRedis.cache.core.load;

import com.github.IRedis.cache.annotation.CacheInterceptor;
import com.github.IRedis.cache.api.ICache;
import com.github.IRedis.cache.api.ICacheLoad;
import com.github.IRedis.cache.core.core.Cache;
import com.github.IRedis.cache.core.model.PersistAofEntry;
import com.github.IRedis.cache.core.proxy.CacheProxy;
import com.github.IRedis.cache.core.util.CollectionUtil;
import com.github.support.heaven.lang.StringUtil;
import com.github.support.heaven.util.io.FileUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheLoadAof<K, V> implements ICacheLoad<K, V> {

    private static final Logger log = LoggerFactory.getLogger(CacheLoadAof.class);

    private final Gson gson = new Gson();

    private static final Map<String, Method> METHOD_MAP = new HashMap<>();

    static {
        Method[] methods = Cache.class.getMethods();

        for(Method method : methods){
            CacheInterceptor cacheInterceptor = method.getAnnotation(CacheInterceptor.class);

            if(cacheInterceptor != null) {
                if(cacheInterceptor.aof()) {
                    String methodName = method.getName();
                    METHOD_MAP.put(methodName, method);
                }
            }
        }
    }

    private final String dbPath;

    public CacheLoadAof(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) throws InvocationTargetException, IllegalAccessException {
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

            PersistAofEntry entry = gson.fromJson(line, PersistAofEntry.class);

            final String methodName = entry.getMethodName();
            final Object[] objects = entry.getParams();

            final Method method = METHOD_MAP.get(methodName);

            method.invoke(cache, objects);
        }
    }
}
