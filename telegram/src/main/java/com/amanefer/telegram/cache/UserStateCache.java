package com.amanefer.telegram.cache;

import com.amanefer.telegram.util.UserState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserStateCache implements BotCache<Long, UserState> {

    private final Map<Long, UserState> cache = new HashMap<>();


    public UserState putInCache(Long userId, UserState state) {

        return cache.put(userId, state);
    }

    public UserState getFromCache(Long userId) {

        UserState state = cache.get(userId);

        if (state == null) {
            state = UserState.PRIMARY;
        }

        return state;
    }
}
