package com.example.controller.v2;

import com.example.model.dto.UserCreationDTO;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserUpdateDTO;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v2/admin")
@RequiredArgsConstructor
public class AdminAPI {
    private final AdminService adminService;

    @PostMapping("/user")
    public UserDTO createNewUser(@Valid @RequestBody UserCreationDTO userCreationDTO) {
        return adminService.save(userCreationDTO);
    }

    @DeleteMapping("/user")
    public String deleteUser(@RequestBody Long id) {
        adminService.delete(id);
        return "User with id " + id + " deleted";
    }

    @PatchMapping("/user/{id}")
    public UserDTO updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO userUpdateDTO) {
        return adminService.update(id, userUpdateDTO);
    }
}
