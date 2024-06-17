package com.example.service;

import com.example.config.properties.RoleProperties;
import com.example.exceptions.NotAllowedDeleteOperationException;
import com.example.exceptions.RoleNotFoundException;
import com.example.exceptions.UserNotFoundException;
import com.example.model.User;
import com.example.model.dto.UserCreationDTO;
import com.example.model.dto.UserDTO;
import com.example.model.dto.UserUpdateDTO;
import com.example.model.dto.mappers.UserDTOMapper;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.utils.AuthUtils;
import com.example.utils.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final UserDTOMapper userDTOMapper;
    private final UserRepository userRepository;
    private final AuthUtils authUtils;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RoleProperties roleProperties;
    private final UserValidator userValidator;
    private final TravelTimeService travelTimeService;


    public UserDTO save(UserCreationDTO userCreationDTO) {
        User user = userDTOMapper.convertToUser(userCreationDTO);
        assignDefaultRoleIfNotPresent(user);
        assignNamePasswordIfNotPresent(user);

        userValidator.validate(user);
        UserDTO userDTO = userDTOMapper.convertToUserDTO(userRepository.save(user));
        travelTimeService.setDepartureTimeAndTravelTime(userDTO);
        return userDTO;
    }



    public UserDTO update(Long id, UserUpdateDTO userUpdateDTO) {
        User user = userDTOMapper.convertToUser(userUpdateDTO);
        user = userRepository.save(updateUserFields(id, user));
        UserDTO userDTO = userDTOMapper.convertToUserDTO(user);
        travelTimeService.setDepartureTimeAndTravelTime(userDTO);
        return userDTO;
    }

    public void delete(Long id) {
        if (authUtils.getAuthenticatedUser().getId().equals(id)) {
            throw new NotAllowedDeleteOperationException("You can't delete yourself");
        }
        if (userRepository.findById(id).isPresent() && userRepository.findById(id).get().getName().equalsIgnoreCase("admin")) {
            throw new NotAllowedDeleteOperationException("You can't delete the admin");
        }
        userRepository.deleteById(id);
    }

    private User updateUserFields(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        User persistedUser = optionalUser.get();

        userValidator.validate(persistedUser, updatedUser);

        persistedUser.setName(updatedUser.getName());
        persistedUser.setEmail(updatedUser.getEmail());
        persistedUser.setHomeAddress(updatedUser.getHomeAddress());
        persistedUser.setJobAddress(updatedUser.getJobAddress());

        assignDefaultRoleIfNotPresent(updatedUser);
        persistedUser.setRoles(updatedUser.getRoles());

        return persistedUser;
    }

    private void assignNamePasswordIfNotPresent(User user) {
        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode(user.getName()));
        }
    }

    private void assignDefaultRoleIfNotPresent(User user) {
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            setDefaultRole(user);
        }
        if (!hasUserRole(user)) {
            assignRoleUser(user);
        }
    }

    private void assignRoleUser(User user) {
        user.getRoles().add(roleRepository.findByRoleIgnoreCase(roleProperties.getUser()).orElseThrow(() -> new RoleNotFoundException(roleProperties.getUser())));
    }

    private boolean hasUserRole(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getRole().equals(roleProperties.getUser()));
    }

    private void setDefaultRole(User user) {
        String defaultRole = roleProperties.getUser();
        user.setRoles(new HashSet<>(){{
            add(roleRepository.findByRoleIgnoreCase(defaultRole).orElseThrow(() -> new RoleNotFoundException(defaultRole)));
        }});
    }
}
