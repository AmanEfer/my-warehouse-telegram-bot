package com.amanefer.crud.services;

import com.amanefer.crud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.amanefer.crud.models.User;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.mappers.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public UserDto saveUser(User user) {
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(User user) {
        return saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

