package com.example.controller;

import com.example.model.dto.UserDTO;
import com.example.service.RegistrationService;
import com.example.service.UserService;
import com.example.utils.PasswordValidator;
import com.example.utils.UserValidator;
import com.example.utils.mappers.UserDTOMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping
public class UserController {
    private final UserValidator userValidator;
    private final PasswordValidator passwordValidator;
    private final RegistrationService registrationService;
    private final UserDTOMapper userDTOMapper;
    private final UserService userService;

    public UserController(UserValidator userValidator, PasswordValidator passwordValidator, RegistrationService registrationService, UserDTOMapper userDTOMapper, UserService userService) {
        this.userValidator = userValidator;
        this.passwordValidator = passwordValidator;
        this.registrationService = registrationService;
        this.userDTOMapper = userDTOMapper;
        this.userService = userService;
    }

    @GetMapping
    public String getUserPageByDefault() {
        return "redirect:/user";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String getUserPage(Model model) {
        model.addAttribute("user", userService.getAuthenticatedUser());
        return "user";
    }

    @GetMapping("/denied")
    public String getUserPage() {
        return "denied";
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "signup";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {

        userValidator.validate(userDTOMapper.mapToUser(userDTO), bindingResult);
        passwordValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "signup";
        }

        registrationService.registerNewUserAccount(userDTO);
        model.addAttribute("user", userDTO);

        return "redirect:/user"; // перенаправляет на страницу аутентификации из-за настроек прав
    }
}
