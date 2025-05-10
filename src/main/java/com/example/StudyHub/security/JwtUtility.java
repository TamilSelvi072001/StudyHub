package com.example.StudyHub.security;

import com.example.StudyHub.model.Role;
import com.example.StudyHub.model.User;
import com.example.StudyHub.repository.UserRepository;
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

    private static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512); // Secret key
    private final int jwtExpirationTime = 86400000; // 24 hours in milliseconds
    private final UserRepository userRepository;

    public JwtUtility(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String email) {
        // Fetch user by email
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with email: " + email);
        }

        User user = optionalUser.get(); // Get the User object
        Set<Role> roles = user.getRole(); // Retrieve roles

        // Log info
        System.out.println("Generating token for email: " + email);
        System.out.println("Signing Key: " + secretKey);

        return Jwts.builder()
                .setSubject(email) // Subject is email now
                .claim("roles", roles.stream()
                        .map(Role::getRoleType)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime))
                .signWith(secretKey)
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject(); // email is stored as subject
    }

    public Set<String> extractRoles(String token) {
        String rolesString = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String.class);

        return Set.of(rolesString.split(","));
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}