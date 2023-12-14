package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Slf4j
@Controller
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

    @GetMapping
    public String showAllPeople(Model model) {
        log.info("AdminController.showAllPeople()");
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userService.getUserByUsername(username));
        model.addAttribute("newUser", new User());
        return "admin/admin";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("newUser") @Valid User user,
                             @RequestParam("selectedRole") String selectedRole,
                             BindingResult bindingResult) {
        log.info("AdminController.createUser()");
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "redirect:/admin/new";
        }

        registrationService.register(user, selectedRole);

        return "redirect:/admin";
    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id,
                             @RequestParam("selectedRole") String selectedRole) {
        log.info("AdminController.updateUser()");
        user.getRole().add(new Role(selectedRole));

        userService.updateUser(id, user);

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        log.info("AdminController.deleteUser()");
        userService.deleteUser(id);

        return "redirect:/admin";
    }
}
