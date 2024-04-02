package com.amanefer.crud.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@AllArgsConstructor
@ToString
@Entity
@Table(name = "my_user", schema = "my_db_bot")
public class User {

    private static final long serialVersionUID = 1;

    @Id
    @Column(name = "id")
    private Long id;

    @NotEmpty(message = "Username cannot be empty")
    @Size(min = 2, max = 20, message = "Username should be from 2 to 20 symbols")
    @Column(name = "username")
    private String username;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", schema = "my_db_bot",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "role")
    private Set<Role> role = new HashSet<>();

}
