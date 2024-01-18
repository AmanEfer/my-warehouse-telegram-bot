package com.amanefer.crud.services;

import com.amanefer.crud.models.Role;

import java.util.List;

public interface RoleService {

    Role getRoleByName(String name);

    List<Role> getAllRoles();
}
