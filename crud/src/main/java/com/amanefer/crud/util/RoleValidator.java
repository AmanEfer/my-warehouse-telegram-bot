package com.amanefer.crud.util;

import com.amanefer.crud.models.Role;
import com.amanefer.crud.models.User;
import org.springframework.stereotype.Component;

@Component
public class RoleValidator {

    public User addRole(User user, String selectedRole) {
        if (!validate(user, selectedRole)) {
            user.getRole().add(new Role(selectedRole));
        }

        return user;
    }

    private boolean validate(User user, String checkingRole) {
        return user.getRole().stream()
                .map(Role::getName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase(checkingRole));
    }
}
