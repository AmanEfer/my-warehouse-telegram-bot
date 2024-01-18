package com.amanefer.crud.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.models.User;
import com.amanefer.crud.services.RegistrationService;
import com.amanefer.crud.services.UserService;
import com.amanefer.crud.util.UserValidator;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;


    @Autowired
    public AdminController(UserService userService, UserValidator userValidator,
                           RegistrationService registrationService) {
        this.userService = userService;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    //    @GetMapping
//    public String showAllPeople(Model model) {
////        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//
//        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("user", userService.getUserByUsername(""));
//        model.addAttribute("newUser", new User());
//        return "admin/admin";
//    }
    @GetMapping
    public List<UserDto> getAllPeople() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userService.getUser(id);
    }

    //    @PostMapping("/new")
//    public String createUser(@ModelAttribute("newUser") @Valid User user,
//                             @RequestParam("selectedRole") String selectedRole,
//                             BindingResult bindingResult) {
//        userValidator.validate(user, bindingResult);
//
//        if (bindingResult.hasErrors()) {
//            return "redirect:/admin/new";
//        }
//
//        registrationService.register(user, selectedRole);
//
//        return "redirect:/admin";
//    }
    @PostMapping("/new")
    public UserDto createUser(@RequestBody @Valid User user,
                              @RequestParam("selectedRole") String selectedRole) {
        return registrationService.register(user, selectedRole);
    }

    //    @PatchMapping("/{id}")
//    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id,
//                             @RequestParam("selectedRole") String selectedRole) {
//        user.getRole().add(new Role(selectedRole));
//
//        userService.updateUser(id, user);
//
//        return "redirect:/admin";
//    }
    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    //    @DeleteMapping("/{id}")
//    public String deleteUser(@PathVariable("id") Long id) {
//        userService.deleteUser(id);
//
//        return "redirect:/admin";
//    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);

        return new ResponseEntity<>(String.format("User with ID = %s was deleted", id),
                HttpStatus.OK);
    }
}
