package com.example.StudyHub.controller;

import com.example.StudyHub.security.JwtUtility;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private JwtUtility jwtUtility;

    @Value("${role.admin}")
    private String roleAdmin;

    @Value("${role.user}")
    private String roleUser;

    @GetMapping("/protected-data")
    public ResponseEntity<String> getProtectedData(@RequestHeader("Authorization") String token){
        if (token != null && token.startsWith("Bearer ")) {
            String jwtToken = token.substring(7);
            try {
                if (jwtUtility.isValidToken(jwtToken)) {
                    String email = jwtUtility.extractEmail(jwtToken); // ✅ use email
                    Set<String> roles = jwtUtility.extractRoles(jwtToken);

                    if (roles.contains(roleAdmin)) {
                        return ResponseEntity.ok("Welcome " + email + ". Here is " + roles + " data.");
                    } else if (roles.contains(roleUser)) {
                        return ResponseEntity.ok("Welcome " + email + ". Here is " + roles + " data.");
                    } else {
                        return ResponseEntity.status(403).body("Access Denied. You are neither admin nor user.");
                    }
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid Token");
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header invalid");
    }
}