package com.example.service;

import com.example.model.User;
import com.example.model.security.AppUserDetails;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByNameIgnoreCase(username);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("No user found with username: " + username);
        }

        return new AppUserDetails(user.get());
    }
}
