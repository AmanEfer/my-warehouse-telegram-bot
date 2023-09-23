package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        System.out.println("UserServiceImpl.getAllUsers()");  // System.out.println()
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        System.out.println("UserServiceImpl.getUser()");  // System.out.println()
        return userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User getUserByUsername(String username) {
        System.out.println("UserServiceImpl.getUserByUsername()");  // System.out.println()
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        System.out.println("UserServiceImpl.saveUser()");  // System.out.println()
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(Long id, User user) {
        System.out.println("UserServiceImpl.updateUser()");  // System.out.println()
        user.setId(id);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        System.out.println("UserServiceImpl.deleteUser()");  // System.out.println()
        userRepository.deleteById(id);
    }
}
