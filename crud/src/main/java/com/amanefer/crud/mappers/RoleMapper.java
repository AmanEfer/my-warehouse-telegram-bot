package com.amanefer.crud.mappers;

import com.amanefer.crud.models.RoleModel;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import com.amanefer.crud.dto.RoleDto;
import com.amanefer.crud.entities.Role;

@Component
@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface RoleMapper extends BaseMapper<RoleDto, RoleModel, Role> {

}
