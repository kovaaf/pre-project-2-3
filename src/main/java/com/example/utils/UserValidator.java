package com.example.utils;

import com.example.model.User;
import com.example.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@RequiredArgsConstructor
@Component
public class UserValidator implements Validator {
    private final AdminService adminService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;

        if (isNewUser(user) && isUsernameTaken(user)) {
            errors.rejectValue("name", "", "This username is already taken");
        } else if (!isNewUser(user) && isUsernameTaken(user) && !isNameTheSame(user)) {
            errors.rejectValue("name", "", "This username is already taken");
        }
    }

    private static boolean isNewUser(User user) {
        return user.getId() == null;
    }

    private boolean isNameTheSame(User user) {
        return adminService.findById(user.getId()).getName().equalsIgnoreCase(user.getName());
    }

    private boolean isUsernameTaken(User user) {
        return adminService.findByNameIgnoreCase(user.getName()).isPresent();
    }
}
