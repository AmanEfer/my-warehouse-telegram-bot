package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Slf4j
@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final RoleService roleService;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator,
                          RoleService roleService) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.roleService = roleService;
    }

    @GetMapping("/login")
    public String loginPage() {
        log.info("AuthController.loginPage()");
        return "/auth/login";
    }

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        log.info("AuthController.registrationPage()");
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService);

        return "/auth/registration";
    }

    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      @RequestParam("selectedRole") String selectedRole,
                                      BindingResult bindingResult) {
        log.info("AuthController.performRegistration()");
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            log.info("AuthController.performRegistration hasErrors");
            return "/auth/registration";
        }

        registrationService.register(user, selectedRole);

        return "redirect:/";
    }
}
