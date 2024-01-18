package com.amanefer.crud.dto;

import com.amanefer.crud.models.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private Set<Role> role = new HashSet<>();
    private String lastName;
    private String department;
    private int salary;
}
