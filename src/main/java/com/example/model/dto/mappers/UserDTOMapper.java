package com.example.model.dto.mappers;

import com.example.model.User;
import com.example.model.dto.UserCreationDTO;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.dto.UserUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDTOMapper {
    private final RoleMapper roleMapper;

    public User convertToUser(UserRegisterDTO userRegisterDTO) {
        User user = new User();
        user.setName(userRegisterDTO.getName());
        user.setEmail(userRegisterDTO.getEmail());
        return user;
    }

    public User convertToUser(UserCreationDTO userCreationDTO) {
        User user = new User();
        user.setName(userCreationDTO.getName());
        user.setEmail(userCreationDTO.getEmail());
        user.setRoles(roleMapper.convertToRoleSet(userCreationDTO.getRoles()));
        user.setHomeAddress(userCreationDTO.getHomeAddress());
        user.setJobAddress(userCreationDTO.getJobAddress());
        return user;
    }

    public User convertToUser(UserUpdateDTO userUpdateDTO) {
        User user = new User();
        user.setName(userUpdateDTO.getName());
        user.setEmail(userUpdateDTO.getEmail());
        user.setRoles(roleMapper.convertToRoleSet(userUpdateDTO.getRoles()));
        user.setHomeAddress(userUpdateDTO.getHomeAddress());
        user.setJobAddress(userUpdateDTO.getJobAddress());
        return user;
    }

    public UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        userDTO.setHomeAddress(user.getHomeAddress());
        userDTO.setJobAddress(user.getJobAddress());
        return userDTO;
    }
}
