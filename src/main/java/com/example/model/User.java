package com.example.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty(message = "Name should not be empty")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name should contain only letters")
    @Column(unique = true)
    private String name; // уникальное значение
    @Email(message = "Email should be valid")
    private String email;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    private Set<Role> roles;
    private String homeAddress;
    private String jobAddress;

    public String getRolesList() {
        return roles.stream()
                .map(Role::getRole)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.joining(", "));
    }
}
