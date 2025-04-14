package com.example.StudyHub.security;

import com.example.StudyHub.service.CustomerUserDetailService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtUtility jwtUtility;
    private CustomerUserDetailService customerUserDetailService;

    public JwtAuthenticationFilter(JwtUtility jwtUtility, CustomerUserDetailService customerUserDetailService) {
        this.jwtUtility = jwtUtility;
        this.customerUserDetailService = customerUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // Remove "Bearer " prefix
            System.out.println(jwtUtility.extractUserName(token));
            try {
                System.out.println(jwtUtility.extractUserName(token));
                username = jwtUtility.extractUserName(token);

            } catch (Exception e) {
                System.out.println("check");
                System.out.println("Invalid JWT token: " + e.getMessage());
            }
        }

        // Set authentication if username is valid and not already authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails=customerUserDetailService.loadUserByUsername(username);

            // Token is valid
            if (jwtUtility.isValidToken(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
