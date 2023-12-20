package ru.kata.spring.security.demo.services;

import ru.kata.spring.security.demo.models.Role;

import java.util.List;

public interface RoleService {

    Role getRoleByName(String name);

    List<Role> getAllRoles();
}
