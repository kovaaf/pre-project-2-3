package com.example.service;

import com.example.config.properties.RoleProperties;
import com.example.exceptions.NotAllowedDeleteOperationException;
import com.example.exceptions.RoleNotFoundException;
import com.example.exceptions.UserNotFoundException;
import com.example.model.Role;
import com.example.model.User;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import com.example.service.shared.HasAuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService implements HasAuthUser {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleProperties roleProperties;


    public List<User> index() {
        return userRepository.findAll();
    }

    public void save(User user) {
        assignDefaultRoleIfNotPresent(user);
        assignNamePasswordIfNotPresent(user);
        userRepository.save(user);
    }

    private void assignNamePasswordIfNotPresent(User user) {
        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode(user.getName()));
        }
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> findByNameIgnoreCase(String name) {
        return userRepository.findUserByNameIgnoreCase(name).stream().findAny();
    }

    public void update(Long id, User user) {
        userRepository.save(updateUserFields(id, user));
    }

    private User updateUserFields(Long id, User updatedUser) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }

        User persistedUser = optionalUser.get();
        persistedUser.setName(updatedUser.getName());
        persistedUser.setEmail(updatedUser.getEmail());

        assignDefaultRoleIfNotPresent(updatedUser);
        persistedUser.setRoles(updatedUser.getRoles());

        return persistedUser;
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
        return user.getRoles().stream().anyMatch(role -> role.getRole().equals(roleProperties.getUser()));
    }

    private void setDefaultRole(User user) {
        String defaultRole = roleProperties.getUser();
        user.setRoles(new HashSet<>(){{
            add(roleRepository.findByRoleIgnoreCase(defaultRole).orElseThrow(() -> new RoleNotFoundException(defaultRole)));
        }});
    }

    public void delete(Long id) {
        if (getAuthenticatedUser().getId().equals(id)) {
            throw new NotAllowedDeleteOperationException("You can't delete yourself");
        } if (userRepository.findById(id).isPresent() && userRepository.findById(id).get().getName().equalsIgnoreCase("admin")) {
            throw new NotAllowedDeleteOperationException("You can't delete the admin");
        }
        userRepository.deleteById(id);
    }

    public List<Role> showRoles() {
        return roleRepository.findAll();
    }
}
