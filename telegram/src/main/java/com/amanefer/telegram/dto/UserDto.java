package com.amanefer.telegram.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private long id;
    private String username;
    private Set<RoleDto> role = new HashSet<>();

    @Override
    public String toString() {
        return String.format("id: %d,\n" +
                             "username: %s,\n" +
                             "role: %s",
                             id,
                             username,
                             role.stream().findFirst().get()
                                     .getName().replace("ROLE_", "").toLowerCase());
    }
}
