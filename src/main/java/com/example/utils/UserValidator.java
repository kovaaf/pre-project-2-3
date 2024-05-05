package com.example.utils;

import com.example.model.User;
import com.example.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        User user = (User) target;
        // Ошибка, если:
        if (isNewUser(user) && isUsernameTaken(user)) {
            // пользователь новый и пытается взять существующее имя
            errors.rejectValue("name", "", "This username is already taken");
        } else if (!isNewUser(user) && (isUsernameTaken(user) && !isNameTheSame(user))) {
            // редактируем существующего пользователя и берём существующее имя и оно не равно ему же(для редактирования регистра)
            errors.rejectValue("name", "", "This username is already taken");
        }

        // По условию не требуется проверка на уникальность email
//        if (userService.findByEmail(user.getEmail()).isPresent()) {
//            errors.rejectValue("email", "", "This email is already taken");
//        }
    }

    private static boolean isNewUser(User user) {
        return user.getId() == null;
    }

    private boolean isNameTheSame(User user) {
        return userService.findById(user.getId()).getName().equalsIgnoreCase(user.getName());
    }

    private boolean isUsernameTaken(User user) {
        return userService.findByNameIgnoreCase(user.getName()).isPresent();
    }
}
