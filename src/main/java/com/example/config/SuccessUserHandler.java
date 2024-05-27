package com.example.config;

import com.example.config.properties.RoleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {
    private final RoleProperties roleProperties;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (roles.contains(roleProperties.getAdmin()) || roles.contains(roleProperties.getUser())) {
            httpServletResponse.sendRedirect("/");
        } else {
            httpServletResponse.sendRedirect("/login");
        }
    }
}
