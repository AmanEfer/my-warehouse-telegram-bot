package ru.kata.spring.security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.kata.spring.security.demo.models.Role;
import ru.kata.spring.security.demo.repositories.RoleRepository;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role getRoleByName(String name) {
        return roleRepository.findRoleByName(name)
                .orElseThrow(() -> new IllegalArgumentException("No such role"));//todo нельзя осталять null
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
