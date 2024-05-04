package com.amanefer.telegram.cache;

import com.amanefer.telegram.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProductStateCache implements BotCache<Long, ProductDto> {

    Map<Long, ProductDto> cache = new HashMap<>();


    @Override
    public ProductDto putInCache(Long userId, ProductDto productDto) {

        return cache.put(userId, productDto);
    }

    @Override
    public ProductDto getFromCache(Long userId) {

        return cache.get(userId);
    }
}
