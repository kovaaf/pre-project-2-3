package com.example.service.shared;

import com.example.model.User;
import com.example.model.security.AppUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface HasAuthUser {
    default User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        return userDetails.getUser();
    }
}
