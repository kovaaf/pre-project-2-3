package com.example.controller;

import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getUserPage(Model model) {
        model.addAttribute("user", userService.getAuthenticatedUser());
        return "user";
    }
}
