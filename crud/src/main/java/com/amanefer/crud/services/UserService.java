package com.amanefer.crud.services;

import com.amanefer.crud.models.User;
import com.amanefer.crud.dto.UserDto;

import java.util.List;
import java.util.Optional;


public interface UserService {

    List<UserDto> getAllUsers();

    Optional<User> getUser(Long id);
    Optional<User> getUserByUsername(String username);

    UserDto saveUser(User person);

    UserDto updateUser(User person);

    void deleteUser(Long id);
}
