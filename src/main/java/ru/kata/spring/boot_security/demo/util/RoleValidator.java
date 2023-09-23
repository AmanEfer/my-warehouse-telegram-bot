package ru.kata.spring.boot_security.demo.util;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

@Component
public class RoleValidator {

    public void addRole(User user, String selectedRole) {
        System.out.println("RoleValidator.addRole()");  // System.out.println()
        if (!validate(user, selectedRole)) {
            user.getRole().add(new Role(selectedRole));
        }
    }

    private boolean validate(User user, String checkingRole) {
        System.out.println("RoleValidator.validate()");  // System.out.println()
        return user.getRole().stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase(checkingRole));
    }
}
