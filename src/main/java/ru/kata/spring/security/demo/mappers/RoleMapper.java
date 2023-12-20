package ru.kata.spring.security.demo.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.kata.spring.security.demo.dto.RoleDto;
import ru.kata.spring.security.demo.models.Role;

@Component
@Mapper(componentModel = "spring")
public interface RoleMapper extends MyMapper<Role, RoleDto> {

    @Override
    RoleDto toDto(Role role);

    @Override
    Role toModel(RoleDto roleDto);
}
