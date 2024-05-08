package com.example.controller;

import com.example.model.User;
import com.example.service.AdminService;
import com.example.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final UserValidator userValidator;

    @GetMapping
    public String index(Model model) {
        List<User> userList = adminService.index();
        userList.removeIf(user -> user.getName().equals("admin"));
        model.addAttribute("users", userList);
        return "admin/index";
    }
    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user) {
        return "admin/new";
    }
    @PostMapping
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/new";
        }

        adminService.save(user);

        return "redirect:/admin";
    }
    @GetMapping("/{id}/edit")
    public String edit(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", adminService.findById(id));
        model.addAttribute("roleList", adminService.showRoles());

        return "admin/edit";
    }
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         @PathVariable("id") Long id) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/edit";
        }

        adminService.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") Long id) {
        adminService.delete(id);
        return "redirect:/admin";
    }
}
