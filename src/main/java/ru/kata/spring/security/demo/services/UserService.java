package ru.kata.spring.security.demo.services;

import ru.kata.spring.security.demo.models.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUser(Long id);
    User getUserByUsername(String username);

    void saveUser(User person);

    void updateUser(Long id, User person);

    void deleteUser(Long id);
}
