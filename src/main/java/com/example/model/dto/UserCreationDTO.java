package com.example.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@ToString
public class UserCreationDTO {
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^[A-Za-z]+$", message = "Name should contain only letters")
    private String name;
    @NotNull
    @NotEmpty(message = "Email should not be empty")
    @Email
    private String email;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Long> roles;
    private String homeAddress;
    private String jobAddress;
}
