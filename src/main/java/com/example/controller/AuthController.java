package com.example.controller;

import com.example.model.dto.UserDTO;
import com.example.service.RegistrationService;
import com.example.utils.PasswordValidator;
import com.example.utils.UserValidator;
import com.example.utils.mappers.UserDTOMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    private final PasswordValidator passwordValidator;
    private final RegistrationService registrationService;
    private final UserDTOMapper userDTOMapper;

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDTO());
        return "auth/signup";
    }

    @PostMapping("/registration")
    public String registerUserAccount(@ModelAttribute("user") @Valid UserDTO userDTO, BindingResult bindingResult, Model model) {

        userValidator.validate(userDTOMapper.mapToUser(userDTO), bindingResult);
        passwordValidator.validate(userDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            return "auth/signup";
        }

        registrationService.registerNewUserAccount(userDTO);
        model.addAttribute("user", userDTO);

        return "redirect:/user";
    }
}
