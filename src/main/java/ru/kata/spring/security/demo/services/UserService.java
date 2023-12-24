package ru.kata.spring.security.demo.services;

import ru.kata.spring.security.demo.models.User;
import ru.kata.spring.security.demo.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(Long id);
    UserDto getUserByUsername(String username);

    UserDto saveUser(User person);

    UserDto updateUser(Long id, User person);

    void deleteUser(Long id);
}
