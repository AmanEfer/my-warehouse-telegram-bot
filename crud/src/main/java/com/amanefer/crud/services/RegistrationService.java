package com.amanefer.crud.services;

import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.entities.User;
import com.amanefer.crud.mappers.UserMapper;
import com.amanefer.crud.services.user.UserService;
import com.amanefer.crud.util.RoleValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserService userService;
    private final RoleValidator roleValidator;
    private final UserMapper userMapper;

    @Transactional
    public UserDto register(UserDto dto, String roleName) {

        User savedUser = userService.getUserByUsernameAsOptional(dto.getUsername()).orElseGet(
                () -> {
                    User user = roleValidator.addRole(
                            userMapper.fromModelToEntity(userMapper.fromDtoToModel(dto)),
                            roleName
                    );

                    return userService.saveUserAsEntity(user);
                }
        );

        return userMapper.fromModelToDto(userMapper.fromEntityToModel(savedUser));
    }

}
