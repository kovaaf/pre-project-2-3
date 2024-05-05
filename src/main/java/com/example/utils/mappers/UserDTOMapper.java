package com.example.utils.mappers;

import com.example.model.User;
import com.example.model.dto.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        return userDTO;
    }

    public User mapToUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        return user;
    }
}
