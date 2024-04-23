package com.amanefer.telegram.cache;

public interface BotCache<K, V> {

    V putInCache(K userId, V v);

    V getFromCache(K userId);
}
