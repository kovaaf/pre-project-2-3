package com.example.service;

import com.example.config.properties.RoleProperties;
import com.example.exceptions.RoleNotFoundException;
import com.example.exceptions.UserNotFoundException;
import com.example.model.Role;
import com.example.model.User;
import com.example.model.security.AppUserDetails;
import com.example.repository.RoleRepository;
import com.example.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleProperties roleProperties;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, RoleProperties roleProperties) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleProperties = roleProperties;
    }


    public List<User> index() {
        return userRepository.findAll();
    }

    public void save(User user) {
        if (user.getRoles() == null) { // если роли не заданы, назначаем роль по умолчанию
            setDefaultRole(user);
        }
        if (user.getPassword() == null) { // если пароль не задан, назначаем его имя в качестве пароля
            user.setPassword(passwordEncoder.encode(user.getName()));
        }
        userRepository.save(user);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> findByNameIgnoreCase(String name) {
        return userRepository.findUserByNameIgnoreCase(name).stream().findAny();
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findUserByEmailIgnoreCase(email).stream().findAny();
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

        if (updatedUser.getRoles() == null || updatedUser.getRoles().isEmpty()) { // если оставили чекбоксы ролей пустыми, назначаем роль юзера
            setDefaultRole(updatedUser);
        }
        if (!hasUserRole(updatedUser)) { // если в списке ролей нет юзера, назначаем роль юзера
            assignRoleUser(updatedUser);
        }
        persistedUser.setRoles(updatedUser.getRoles());

        return persistedUser;
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
            throw new RuntimeException("You can't delete yourself");
        } if (userRepository.findById(id).isPresent() && userRepository.findById(id).get().getName().equalsIgnoreCase("admin")) {
            throw new RuntimeException("You can't delete the admin");
        }
        userRepository.deleteById(id);
    }

    public List<Role> showRoles() {
        return roleRepository.findAll();
    }
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
