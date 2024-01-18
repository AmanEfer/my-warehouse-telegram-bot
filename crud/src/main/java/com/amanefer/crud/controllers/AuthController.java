package com.amanefer.crud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.models.User;
import com.amanefer.crud.services.RegistrationService;
import com.amanefer.crud.services.RoleService;

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
