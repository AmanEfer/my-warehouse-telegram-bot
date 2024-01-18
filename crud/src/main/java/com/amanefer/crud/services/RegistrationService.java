package com.amanefer.crud.services;

import com.amanefer.crud.mappers.UserMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.models.User;
import com.amanefer.crud.util.RoleValidator;

@Slf4j
@Service
public class RegistrationService {

    private final UserService userService;
//    private final PasswordEncoder passwordEncoder;
    private final RoleValidator roleValidator;
    private final UserMapper userMapper;

    @Autowired
    public RegistrationService(UserService userService, RoleValidator roleValidator, UserMapper userMapper) {
        this.userService = userService;
        this.roleValidator = roleValidator;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDto register(User user, String roleName) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        roleValidator.addRole(user, roleName);

        return userService.saveUser(user);
    }
}
