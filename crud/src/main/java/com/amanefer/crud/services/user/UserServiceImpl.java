package com.amanefer.crud.services.user;

import com.amanefer.crud.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.amanefer.crud.entities.User;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.mappers.UserMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public static final String USER_NOT_FOUND_MESSAGE = "User not found";
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUsers() {

        return userRepository.findAll().stream()
                .map(userMapper::fromEntityToModel)
                .map(userMapper::fromModelToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long id) {

        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

        return userMapper.fromModelToDto(userMapper.fromEntityToModel(foundUser));
    }

    @Override
    public UserDto getUserByUsername(String username) {

        User foundUser = userRepository.findUserByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(USER_NOT_FOUND_MESSAGE));

        return userMapper.fromModelToDto(userMapper.fromEntityToModel(foundUser));
    }

    @Override
    public Optional<User> getUserByUsernameAsOptional(String username) {

        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public UserDto saveUser(UserDto user) {

        User savedUser = userRepository.save(userMapper.fromModelToEntity(userMapper.fromDtoToModel(user)));

        return userMapper.fromModelToDto(userMapper.fromEntityToModel(savedUser));
    }

    @Override
    public User saveUserAsEntity(User user) {

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(UserDto user) {

        return saveUser(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        userRepository.deleteById(id);
    }
}

