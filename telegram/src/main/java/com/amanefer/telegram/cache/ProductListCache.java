package com.amanefer.telegram.cache;

import com.amanefer.telegram.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProductListCache implements BotCache<Long, List<ProductDto>> {

    private final Map<Long, List<ProductDto>> cache = new HashMap<>();


    @Override
    public List<ProductDto> putInCache(Long userId, List<ProductDto> productList) {

        return cache.put(userId, productList);
    }

    @Override
    public List<ProductDto> getFromCache(Long userId) {

        return cache.get(userId);
    }
}
