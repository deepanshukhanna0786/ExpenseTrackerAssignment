package com.example.expensetrackerassignment.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove "Bearer " prefix

            // Validate the token and get the email and roles
            JwtUtils.TokenData tokenData = JwtUtils.validateToken(token);

            if (tokenData != null) {
                // If token is valid, authenticate the user and set roles as authorities
                List<SimpleGrantedAuthority> authorities = tokenData.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  // Add "ROLE_" prefix
                        .collect(Collectors.toList());

                // Create the authentication token with roles
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(tokenData.getEmail(), null, authorities);

                // Set the authentication in the SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}
