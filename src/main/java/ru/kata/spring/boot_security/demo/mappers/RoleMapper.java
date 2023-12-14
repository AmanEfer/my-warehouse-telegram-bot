package ru.kata.spring.boot_security.demo.mappers;

import org.mapstruct.Mapper;
import ru.kata.spring.boot_security.demo.dto.RoleDto;
import ru.kata.spring.boot_security.demo.models.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper extends MyMapper<Role, RoleDto> {

    @Override
    RoleDto toDto(Role role);

    @Override
    Role toModel(RoleDto roleDto);
}
