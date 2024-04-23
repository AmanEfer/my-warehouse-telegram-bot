package com.amanefer.crud.controllers;

import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.services.RegistrationService;
import com.amanefer.crud.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crud/users")
@RequiredArgsConstructor
public class UserController {

    public static final String USER_DELETE_MESSAGE = "User with ID = %s was deleted";

    private final UserService userService;
    private final RegistrationService registrationService;


    @GetMapping
    public List<UserDto> getAllPeople() {

        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") long id) {

        return userService.getUser(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@RequestBody UserDto dto,
                              @RequestParam("selectedRole") String selectedRole) {

        return registrationService.register(dto, selectedRole);
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto dto) {

        return userService.updateUser(dto);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userService.deleteUser(id);

        return String.format(USER_DELETE_MESSAGE, id);
    }

}
