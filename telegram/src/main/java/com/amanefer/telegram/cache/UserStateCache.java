package com.amanefer.telegram.cache;

import com.amanefer.telegram.util.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStateCache implements BotCache<Long, BotState> {

    private final Map<Long, BotState> cache = new HashMap<>();


    public BotState putInCache(Long userId, BotState state) {

        return cache.put(userId, state);
    }

    public BotState getFromCache(Long userId) {

        BotState state = cache.get(userId);

        if (state == null) {
            state = BotState.PRIMARY;
        }

        return state;
    }
}
