package com.example.model.dto;

import com.example.model.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class UserDTO {
    private Long id;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name should contain only letters")
    private String name;
    @NotNull
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    private Set<Role> roles;
    private String homeAddress;
    private String jobAddress;
    private LocalTime departureTime;
    private LocalTime travelTime;

    public String getRolesList() {
        return roles.stream()
                .map(Role::getRole)
                .map(s -> s.replace("ROLE_", ""))
                .collect(Collectors.joining(", "));
    }
}
