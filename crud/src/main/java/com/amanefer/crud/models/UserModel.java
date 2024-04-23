package com.amanefer.crud.models;

import com.amanefer.crud.entities.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserModel {

    private Long id;
    private String username;
    private Set<Role> role = new HashSet<>();
}
