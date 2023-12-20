package ru.kata.spring.security.demo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.security.demo.models.User;
import ru.kata.spring.security.demo.repositories.UserRepository;

import java.util.Optional;

@Slf4j
@Component
public class UserValidator implements Validator {

    private final UserRepository userRepository;

    @Autowired
    public UserValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        log.info("UserValidator.supports()");
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        log.info("UserValidator.validate()");
        User checkedPerson = (User) target;
        Optional<User> foundUser = userRepository.findUserByUsername(checkedPerson.getUsername());

        if (foundUser.isPresent()) {
            errors.rejectValue("username", "", "User already exists");
        }
    }
}
