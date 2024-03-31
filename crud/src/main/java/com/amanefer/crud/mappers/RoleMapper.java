package com.amanefer.crud.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.amanefer.crud.dto.RoleDto;
import com.amanefer.crud.models.Role;

@Component
@Mapper(componentModel = "spring")
public interface RoleMapper extends BaseMapper<Role, RoleDto> {

    @Override
    RoleDto toDto(Role role);

    @Override
    Role toEntity(RoleDto roleDto);
}
