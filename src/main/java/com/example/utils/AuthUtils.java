package com.example.utils;

import com.example.model.User;
import com.example.model.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
