package com.amanefer.telegram.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private Set<RoleDto> role = new HashSet<>();


    @Override
    public String toString() {

        String role = this.role.stream()
                .findFirst()
                .map(roleDto -> roleDto.getName().replace("ROLE_", "").toLowerCase())
                .orElse("not assigned");

        return String.format("""
                        id: %d
                        username: %s
                        role: %s""",
                id, username, role);
    }
}
