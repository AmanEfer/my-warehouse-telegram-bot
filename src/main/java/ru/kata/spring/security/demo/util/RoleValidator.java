package ru.kata.spring.security.demo.util;

import org.springframework.stereotype.Component;
import ru.kata.spring.security.demo.models.Role;
import ru.kata.spring.security.demo.models.User;

@Component
public class RoleValidator {

    public void addRole(User user, String selectedRole) {
        if (!validate(user, selectedRole)) {
            user.getRole().add(new Role(selectedRole));
        }
    }

    private boolean validate(User user, String checkingRole) {
        return user.getRole().stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase(checkingRole));
    }
}
