package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RegistrationService;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.util.UserValidator;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
//    private final RoleService roleService;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;


    @Autowired
    public AdminController(UserService userService, /*RoleService roleService,*/ UserValidator userValidator,
                           RegistrationService registrationService) {
        this.userService = userService;
//        this.roleService = roleService;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping
    public String showAllPeople(Model model) {
        System.out.println("AdminController.showAllPeople()");  // System.out.println()
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("user", userService.getUserByUsername(username));
        model.addAttribute("newUser", new User());
        return "admin/admin";
    }

//    @GetMapping("/{id}")
//    public String showUser(@PathVariable("id") Long id, Model model) {
//        System.out.println("AdminController.showUser()");  // System.out.println()
//        model.addAttribute("user", userService.getUser(id));
//
//        return "admin/show";
//    }

//    @GetMapping("/new")
//    public String newUser(Model model) {
//        System.out.println("AdminController.newUser()");  // System.out.println()
//        model.addAttribute("user", new User());
//        model.addAttribute("roles", roleService);
//
//        return "admin/new";
//    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("newUser") @Valid User user,
                             @RequestParam("selectedRole") String selectedRole,
                             BindingResult bindingResult) {
        System.out.println("AdminController.createUser()");  // System.out.println()
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "redirect:/admin/new";
        }

        registrationService.register(user, selectedRole);

        return "redirect:/admin";
    }

//    @GetMapping("/{id}/edit")
//    public String editUser(@PathVariable("id") Long id, Model model) {
//        System.out.println("AdminController.editUser()");  // System.out.println()
//        model.addAttribute("user", userService.getUser(id));
//
//        return "admin/edit";
//    }

    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") User user, @PathVariable("id") Long id,
                             @RequestParam("selectedRole") String selectedRole) {
        System.out.println("AdminController.updateUser()");  // System.out.println()
        System.out.println(user.getId());
        System.out.println(user.getPassword());
        System.out.println(user.getUsername());
        System.out.println(user.getLastName());
        System.out.println(user.getDepartment());
        System.out.println(user.getSalary());
        user.getRole().add(new Role(selectedRole));

        userService.updateUser(id, user);

        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        System.out.println("AdminController.deleteUser()");  // System.out.println()
        userService.deleteUser(id);

        return "redirect:/admin";
    }
}
