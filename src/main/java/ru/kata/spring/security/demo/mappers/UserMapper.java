package ru.kata.spring.security.demo.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.kata.spring.security.demo.dto.UserDto;
import ru.kata.spring.security.demo.models.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends MyMapper<User, UserDto> {

    @Override
    UserDto toDto(User user);

    @Override
    User toModel(UserDto userDto);
}
