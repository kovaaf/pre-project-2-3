package com.example.model.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class UserDTO {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name should contain only letters")
    private String name;
    @NotNull
    @NotEmpty(message = "Password should not be empty")
    private String password;
    private String matchingPassword;
    @NotNull
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
}
