package com.example.utils;

import com.example.model.dto.UserDTO;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PasswordValidator implements Validator {


    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserDTO user = (UserDTO) target;

        if (!user.getPassword().equals(user.getMatchingPassword())) {
            errors.rejectValue("matchingPassword", "", "Passwords do not match");
        }
    }
}
