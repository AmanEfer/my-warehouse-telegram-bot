package ru.kata.spring.boot_security.demo.configs;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class SuccessUserHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, IOException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (roles.contains("ROLE_ADMIN")) {
            System.out.println("SuccessUserHandler: ADMIN"); // System.out.println()
            httpServletResponse.sendRedirect("/admin");
        } else if (roles.contains("ROLE_USER")) {
            System.out.println("SuccessUserHandler: USER"); // System.out.println()
            httpServletResponse.sendRedirect("/user");
        }
    }
}