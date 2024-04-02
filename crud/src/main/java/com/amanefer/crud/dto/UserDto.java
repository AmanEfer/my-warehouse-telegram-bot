package com.amanefer.crud.dto;

import com.amanefer.crud.models.Role;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private long id;
    private String username;
    private Set<Role> role = new HashSet<>();
}
