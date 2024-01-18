package com.amanefer.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.amanefer.crud.models.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByName(String name);
}
