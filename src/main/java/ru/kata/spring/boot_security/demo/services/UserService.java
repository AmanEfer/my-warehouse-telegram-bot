package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(Long id);
    UserDto getUserByUsername(String username);

    void saveUser(User person);

    void updateUser(Long id, User person);

    void deleteUser(Long id);
}
