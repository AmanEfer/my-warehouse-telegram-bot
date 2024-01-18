package com.amanefer.crud.util;

import com.amanefer.crud.models.User;
import com.amanefer.crud.repositories.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User checkedPerson = (User) target;
        Optional<User> foundUser = userRepository.findUserByUsername(checkedPerson.getUsername());

        if (foundUser.isPresent()) {
            errors.rejectValue("username", "", "User already exists");
        }
    }
}
