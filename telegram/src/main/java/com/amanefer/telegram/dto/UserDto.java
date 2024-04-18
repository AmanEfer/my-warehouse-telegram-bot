package com.amanefer.telegram.dto;

import com.amanefer.telegram.state.BotState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private long id;
    private String username;
    private BotState botState;
    private Set<RoleDto> roles = new HashSet<>();


    public UserDto(long id, String username, Set<RoleDto> roles) {
        this.id = id;
        this.username = username;
        this.roles = roles;
    }


    @Override
    public String toString() {

        String role = roles.stream()
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
