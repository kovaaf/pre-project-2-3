package com.example.utils;

import com.example.config.properties.RoleProperties;
import com.example.exceptions.RoleNotFoundException;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@RequiredArgsConstructor
@Component
public class AuthUtils {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleProperties roleProperties;
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticationName = authentication.getName();
        return userRepository.findByNameIgnoreCase(authenticationName).orElse(buildAdminUser(authenticationName));
    }

    private User buildAdminUser(String authenticationName) {
        return User.builder()
                .name(authenticationName)
                .email(authenticationName + "@mail.com")
                .roles(new HashSet<>() {{
                    String userRole = roleProperties.getUser();
                    add(roleRepository.findByRoleIgnoreCase(userRole).orElseThrow(() -> new RoleNotFoundException(userRole)));
                    String adminRole = roleProperties.getAdmin();
                    add(roleRepository.findByRoleIgnoreCase(adminRole).orElseThrow(() -> new RoleNotFoundException(adminRole)));
                }})
                .build();
    }
}
