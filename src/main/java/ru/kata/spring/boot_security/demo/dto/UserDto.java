package ru.kata.spring.boot_security.demo.dto;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDto {
    @Id
    private Long id;
    private String username;
    private String password;
    private Set<Role> role = new HashSet<>();
    private String lastName;
    private String department;
    private int salary;
}
