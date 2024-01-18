package com.amanefer.crud.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.models.User;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper extends MyMapper<User, UserDto> {

    @Override
    UserDto toDto(User user);

    @Override
    User toModel(UserDto userDto);
}
