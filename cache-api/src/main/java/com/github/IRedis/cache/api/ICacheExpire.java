package com.github.IRedis.cache.api;

import java.util.Collection;

public interface ICacheExpire<K, V> {

    static final long NONE_EXPIRE = -1;

    void expire(final K key, final long expireAt);

    // 惰性删除
    void refreshExpire(final Collection<K> keyList);

    Long expireTime(final K key);
}
