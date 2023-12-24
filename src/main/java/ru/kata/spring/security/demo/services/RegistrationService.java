package ru.kata.spring.security.demo.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.security.demo.dto.UserDto;
import ru.kata.spring.security.demo.mappers.UserMapper;
import ru.kata.spring.security.demo.models.User;
import ru.kata.spring.security.demo.util.RoleValidator;

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
