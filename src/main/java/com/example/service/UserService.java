package com.example.service;

import com.example.model.Role;
import com.example.model.User;
import com.example.model.dto.UserDTO;
import com.example.model.dto.mappers.UserDTOMapper;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthUtils authUtils;
    private final UserDTOMapper userDTOMapper;
    private final TravelTimeService travelTimeService;

    public List<UserDTO> getUsersDTO() {
        List<User> userList = userRepository.findAll();
        userList.removeIf(user -> user.getName().equals("admin"));
        List<UserDTO> userDTOList = userList.stream().map(userDTOMapper::convertToUserDTO).collect(Collectors.toList());
        userDTOList.forEach(travelTimeService::setDepartureTimeAndTravelTime);
        return userDTOList;
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> findByNameIgnoreCase(String name) {
        return userRepository.findUserByNameIgnoreCase(name).stream().findAny();
    }

    public List<Role> showRoles() {
        return roleRepository.findAll();
    }

    public UserDTO getAuthenticatedUserDTO() {
        return userDTOMapper.convertToUserDTO(authUtils.getAuthenticatedUser());
    }
}
