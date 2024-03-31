package com.amanefer.crud.services;

import com.amanefer.crud.mappers.UserMapper;
import com.amanefer.crud.services.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.models.User;
import com.amanefer.crud.util.RoleValidator;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final RoleValidator roleValidator;
    private final UserMapper userMapper;

    @Transactional
    public UserDto register(User user, String roleName) {
        Optional<User> optional = userService.getUserByUsername(user.getUsername());
        UserDto userDto;
        if (optional.isEmpty()) {
            user = roleValidator.addRole(user, roleName);
            userDto = userService.saveUser(user);
        } else {
            userDto = userMapper.toDto(optional.get());
        }

        return userDto;
    }
}
