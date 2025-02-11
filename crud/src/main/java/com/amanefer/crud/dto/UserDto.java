package com.amanefer.crud.dto;

import com.amanefer.crud.entities.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {

    private long id;
    private String username;
    private Set<Role> role = new HashSet<>();
}
