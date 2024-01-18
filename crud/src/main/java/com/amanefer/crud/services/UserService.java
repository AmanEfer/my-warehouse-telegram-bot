package com.amanefer.crud.services;

import com.amanefer.crud.models.User;
import com.amanefer.crud.dto.UserDto;

import java.util.List;


public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(Long id);
    UserDto getUserByUsername(String username);

    UserDto saveUser(User person);

    UserDto updateUser(User person);

    void deleteUser(Long id);
}
