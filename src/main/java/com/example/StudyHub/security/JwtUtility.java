package com.example.StudyHub.security;

import com.example.StudyHub.model.Role;
import com.example.StudyHub.model.User;
import com.example.StudyHub.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtUtility {

    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final int jwtExpirationTime = 86400000; // 24 hours
    private final UserRepository userRepository;

    public JwtUtility(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        User user = optionalUser.get();
        Set<Role> roles = user.getRole();
        String userName = user.getUserName(); // <-- Use userName field from your User entity

        return Jwts.builder()
                .setSubject(email)
                .claim("name", userName) // Add userName as "name" claim
                .claim("roles", roles.stream()
                        .map(Role::getRoleType)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractName(String token) {
        return extractAllClaims(token).get("name", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Set<String> extractRoles(String token) {
        String rolesString = extractAllClaims(token).get("roles", String.class);
        return Set.of(rolesString.split(","));
    }

    public boolean isValidToken(String token) {
        try {
            extractAllClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}