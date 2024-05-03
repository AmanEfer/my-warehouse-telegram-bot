package com.amanefer.crud.mappers;

import com.amanefer.crud.dto.UserDto;
import com.amanefer.crud.entities.User;
import com.amanefer.crud.models.UserModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper extends BaseMapper<UserDto, UserModel, User> {

}
