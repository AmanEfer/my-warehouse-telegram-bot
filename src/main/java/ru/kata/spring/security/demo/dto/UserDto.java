package ru.kata.spring.security.demo.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kata.spring.security.demo.models.Role;

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
