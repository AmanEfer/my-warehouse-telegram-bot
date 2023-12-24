package ru.kata.spring.security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.security.demo.dto.UserDto;
import ru.kata.spring.security.demo.models.User;
import ru.kata.spring.security.demo.services.RegistrationService;
import ru.kata.spring.security.demo.services.RoleService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final RoleService roleService;

    @Autowired
    public AuthController(RegistrationService registrationService,
                          RoleService roleService) {
        this.registrationService = registrationService;
        this.roleService = roleService;
    }

//    @GetMapping("/login")
//    public String loginPage() {
//        return "/auth/login";
//    }
//
//    @GetMapping("/registration")
//    public String registrationPage(Model model) {
//        model.addAttribute("user", new User());
//        model.addAttribute("roles", roleService);
//
//        return "/auth/registration";
//    }

    @PostMapping("/registration")
    public UserDto performRegistration(@RequestBody User user,
                                       @RequestParam("selectedRole") String selectedRole) {
//        userValidator.validate(user, bindingResult);

//        if (bindingResult.hasErrors()) {
//            return "/auth/registration";
//        }

        return registrationService.register(user, selectedRole);
    }
}
