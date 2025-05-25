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

    private final JwtUtility jwtUtility;
    private final CustomerUserDetailService customerUserDetailService;

    public JwtAuthenticationFilter(JwtUtility jwtUtility, CustomerUserDetailService customerUserDetailService) {
        this.jwtUtility = jwtUtility;
        this.customerUserDetailService = customerUserDetailService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();

        // Allow unauthenticated access to public endpoints
        if (path.startsWith("/api/book")
                || path.startsWith("/auth")
                || path.startsWith("/api/hubdetails")
                || path.startsWith("/api/cities")
                || path.startsWith("/hub")
                || path.startsWith("/images")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                email = jwtUtility.extractEmail(token); // ✅ Use extractEmail instead
            } catch (Exception e) {
                System.out.println("Invalid JWT token: " + e.getMessage());
            }
        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = customerUserDetailService.loadUserByUsername(email);

            if (jwtUtility.isValidToken(token)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        String path = request.getRequestURI();
//
//
//        // List public endpoints to skip JWT filter
//        return path.startsWith("/auth") ||
//                path.startsWith("/hub") ||  // covers both /hub and /hub/...
//                path.startsWith("/api/hubdetails/") ||
//                path.startsWith("/api/cities/") ||
//
//                path.startsWith("/images/");
//
//    }
}