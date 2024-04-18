package com.amanefer.telegram.cache;

import com.amanefer.telegram.state.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class StatesCache {

    private Map<Long, BotState> userStateCache = new HashMap<>();


    public void setUserStateCache(Long userId, BotState state) {

        userStateCache.put(userId, state);
    }

    public BotState getUserStateCache(Long userId) {

        return userStateCache.putIfAbsent(userId, BotState.PRIMARY);
    }
}
