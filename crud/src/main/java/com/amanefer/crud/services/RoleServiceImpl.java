package com.amanefer.crud.services;

import com.amanefer.crud.repositories.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.amanefer.crud.models.Role;

import java.util.List;

@Slf4j
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
