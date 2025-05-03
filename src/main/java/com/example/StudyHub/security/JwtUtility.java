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
    private final int jwtExpirationTime = 86400000; // Token expiration in milliseconds (24 hours)
    private final UserRepository userRepository;

    public JwtUtility(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generateToken(String userName) {
        // Fetch user details safely
        Optional<User> optionalUser = userRepository.findByUserName(userName);

        if (!optionalUser.isPresent()) {
            throw new IllegalArgumentException("User not found with username: " + userName);
        }

        User user = optionalUser.get(); // Get the User object
        Set<Role> roles = user.getRole(); // Retrieve roles

        // Build and return the JWT
        System.out.println("Generating token for user: " + userName);
        System.out.println("Signing Key: " + secretKey); // Logs the secret key used for signing

        return Jwts.builder()
                .setSubject(userName) // Add subject (username)
                .claim("roles", roles.stream() // Add user roles as claims
                        .map(Role::getRoleType)
                        .collect(Collectors.joining(",")))
                .setIssuedAt(new Date()) // Set the issue time
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationTime)) // Set expiration time
                .signWith(secretKey) // Sign the JWT
                .compact(); // Build the token
    }

    public String extractUserName(String token){
        System.out.println("Token received: " + token);
        System.out.println("Signing Key: " + secretKey);
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody().getSubject();
    }

    public Set<String> extractRoles(String token) {
        System.out.println("Token received: " + token);
        System.out.println("Signing Key: " + secretKey);

        String rolesString = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("roles", String.class); // Get the roles as a single comma-separated string

        // Split the roles and convert them into a Set
        return Set.of(rolesString.split(","));
    }

    public boolean isValidToken(String token) {
        System.out.println("Token received: " + token);
        System.out.println("Signing Key: " + secretKey);

        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token); // Parses & validates signature and expiration
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}