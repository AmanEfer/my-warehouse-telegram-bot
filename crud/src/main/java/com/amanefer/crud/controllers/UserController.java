package com.amanefer.crud.controllers;

import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.mappers.UserMapper;
import com.amanefer.crud.services.RegistrationService;
import com.amanefer.crud.services.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crud/users")
public class UserController {

    private final UserService userService;
    private final RegistrationService registrationService;
    private final UserMapper userMapper;

    public UserController(UserService userService,
                          RegistrationService registrationService,
                          UserMapper userMapper) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserDto> getAllPeople() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") long id) {
        return userMapper.toDto(userService.getUser(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto,
                              @RequestParam("selectedRole") String selectedRole) {
        return registrationService.register(userMapper.toEntity(userDto), selectedRole);
    }

    @PatchMapping
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userMapper.toEntity(userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return new ResponseEntity<>(String.format("User with ID = %s was deleted", id),
                HttpStatus.OK);
    }
}
