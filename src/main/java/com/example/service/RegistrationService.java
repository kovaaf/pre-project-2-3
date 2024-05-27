package com.example.service;

import com.example.config.properties.RoleProperties;
import com.example.exceptions.RoleNotFoundException;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserRegisterDTO;
import com.example.model.dto.mappers.UserDTOMapper;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleProperties roleProperties;
    private final UserDTOMapper userDTOMapper;
    private final UserValidator userValidator;

    @Transactional
    public UserDTO registerNewUserAccount(UserRegisterDTO userRegisterDTO) {
        User newUser = setFields(userRegisterDTO);

        userValidator.validate(newUser);

        User save = userRepository.save(newUser);
        userDTOMapper.convertToUserDTO(save);

        return userDTOMapper.convertToUserDTO(save);
    }

    private User setFields(UserRegisterDTO userRegisterDTO) {
        User mappedToUser = userDTOMapper.convertToUser(userRegisterDTO);
        mappedToUser.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        setDefaultRole(mappedToUser);
        return mappedToUser;
    }

    private void setDefaultRole(User user) {
        String userRole = roleProperties.getUser();
        Role defaultUserRole = roleRepository.findByRoleIgnoreCase(userRole)
                .orElseThrow(() -> new RoleNotFoundException(userRole));
        user.setRoles(Set.of(defaultUserRole));
    }
}
