package ru.kata.spring.boot_security.demo.mappers;

import org.mapstruct.Mapper;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

    User toModel(UserDto userDto);
}
