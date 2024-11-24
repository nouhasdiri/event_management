package com.events.event_management.Handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ORGANISATEUR"))) {
            response.sendRedirect("/organizer_dashboard");
        } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"))) {
            response.sendRedirect("/participant_dashboard");
        }
    }
}
