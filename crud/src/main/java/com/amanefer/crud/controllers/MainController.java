package com.amanefer.crud.controllers;

import com.amanefer.crud.mappers.UserMapper;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.services.RegistrationService;
import com.amanefer.crud.services.UserService;
import com.amanefer.crud.util.UserValidator;

import java.util.List;

@RestController
@RequestMapping("/crud")
public class MainController {

    private final UserService userService;
    //    private final UserValidator userValidator;
    private final RegistrationService registrationService;
    private final UserMapper userMapper;

    public MainController(UserService userService,
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

    @PostMapping("/new")
    public UserDto createUser(@RequestBody UserDto userDto,
                              @RequestParam("selectedRole") String selectedRole) {
        return registrationService.register(userMapper.toUser(userDto), selectedRole);
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userMapper.toUser(userDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return new ResponseEntity<>(String.format("User with ID = %s was deleted", id),
                HttpStatus.OK);
    }
}
