package com.example.controller.v2;

import com.example.model.Role;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.service.RegistrationService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
public class UserAPI {
    private final UserService userService;
    private final RegistrationService registrationService;


    @GetMapping("/authUser")
    public UserDTO getAuthUser() {
        return userService.getAuthenticatedUserDTO();
    }

    @GetMapping("/users")
    public List<UserDTO> getUsers() {
        return userService.getUsersDTO();
    }

    @GetMapping("/roles")
    public List<Role> getRoles() {
        return userService.showRoles();
    }

    @PostMapping("/register")
    public UserDTO registerUserAccount(@Valid @RequestBody UserRegisterDTO userRegisterDTO) {
        return registrationService.registerNewUserAccount(userRegisterDTO);
    }
}
