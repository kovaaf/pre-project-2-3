package com.example.utils;

import com.example.exceptions.UsernameAlreadyTakenException;
import com.example.model.User;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserValidator {
    private final UserService userService;

    public void validate(User updatedUser) {
        boolean usernameTaken = isUsernameTaken(updatedUser);
        boolean isNewUser = isNewUser(updatedUser);

        if (isNewUser && usernameTaken) { // новый пользователь пытается взять имя пользователя в БД
            throw new UsernameAlreadyTakenException(updatedUser.getName());
        }
    }

    public void validate(User updatedUser, User persistedUser) {
        boolean usernameTaken = isUsernameTaken(updatedUser);
        boolean isNewUser = isNewUser(updatedUser);
        boolean isUsernameDifferent = isUsernameDifferent(updatedUser, persistedUser);

        if (!isNewUser && isUsernameDifferent && usernameTaken) { // существующий пользователь пытается присвоить чужое имя пользователя
            throw new UsernameAlreadyTakenException(updatedUser.getName());
        }
    }

    private boolean isNewUser(User user) {
        return user.getId() == null;
    }

    private boolean isUsernameDifferent(User updatedUser, User persistedUser) {
        String usersName = userService.findById(updatedUser.getId()).getName();
        String desiredName = persistedUser.getName();
        return !usersName.equalsIgnoreCase(desiredName);
    }

    private boolean isUsernameTaken(User user) {
        return userService.findByNameIgnoreCase(user.getName()).isPresent();
    }
}
