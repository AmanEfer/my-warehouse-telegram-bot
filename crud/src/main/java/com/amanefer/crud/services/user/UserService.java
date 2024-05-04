package com.amanefer.crud.services.user;

import com.amanefer.crud.entities.User;
import com.amanefer.crud.dto.UserDto;

import java.util.List;
import java.util.Optional;


public interface UserService {

    List<UserDto> getAllUsers();

    UserDto getUser(Long id);

    UserDto getUserByUsername(String username);

    Optional<User> getUserByUsernameAsOptional(String username);

    UserDto saveUser(UserDto userDto);

    User saveUserAsEntity(User user);

    UserDto updateUser(UserDto userDto);

    void deleteUser(Long id);
}
