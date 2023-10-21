package com.github.IRedis.cache.core.listener.slow;

import com.github.IRedis.cache.api.ICacheSlowListener;

import java.util.ArrayList;
import java.util.List;

public class CacheSlowListeners {
    private CacheSlowListeners(){}

    private static List<ICacheSlowListener> none(){
        return new ArrayList<>();
    }

    private static ICacheSlowListener defaults(){
        return new CacheSlowListener();
    }
}
