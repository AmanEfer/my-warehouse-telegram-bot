package com.amanefer.telegram.cache;

import com.amanefer.telegram.util.ProductTransferData;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductTransferDataCache implements BotCache<Long, ProductTransferData> {

    private final Map<Long, ProductTransferData> cache = new HashMap<>();


    @Override
    public ProductTransferData putInCache(Long userId, ProductTransferData productTransferData) {

        return cache.put(userId, productTransferData);
    }

    @Override
    public ProductTransferData getFromCache(Long userId) {

        return cache.get(userId);
    }
}
