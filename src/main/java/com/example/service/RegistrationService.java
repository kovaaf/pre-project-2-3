package com.example.service;

import com.example.config.properties.RoleProperties;
import com.example.exceptions.RoleNotFoundException;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.dto.UserDTO;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.utils.mappers.UserDTOMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleProperties roleProperties;
    private final UserDTOMapper userDTOMapper;

    public RegistrationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, RoleProperties roleProperties, UserDTOMapper userDTOMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleProperties = roleProperties;
        this.userDTOMapper = userDTOMapper;
    }

    @Transactional
    public void registerNewUserAccount(UserDTO userDTO) {
        User newUser = setFields(userDTO);

        userRepository.save(newUser);
    }

    private User setFields(UserDTO userDTO) {
        User mappedToUser = userDTOMapper.mapToUser(userDTO);
        mappedToUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
