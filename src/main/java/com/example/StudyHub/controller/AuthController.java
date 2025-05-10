package com.example.StudyHub.controller;

import com.example.StudyHub.dto.RegisterRequest;
import com.example.StudyHub.model.Role;
import com.example.StudyHub.model.User;
import com.example.StudyHub.repository.RoleRepository;
import com.example.StudyHub.repository.UserRepository;
import com.example.StudyHub.security.JwtUtility;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtility jwtUtility;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;


    public AuthController(AuthenticationManager authenticationManager, JwtUtility jwtUtility, UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest registerRequest) {


        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists!");
        }

        if (userRepository.findByPhone(registerRequest.getPhone()).isPresent()) {
            return ResponseEntity.badRequest().body("Phone number already exists!");
        }

        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setDob(registerRequest.getDob());

        Set<Role> roles = new HashSet<>();
        for (String roleStr : registerRequest.getRoles()) {
            Role role = roleRepository.findByRoleType(roleStr)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleStr));
            roles.add(role);
        }

        user.setRole(roles);
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginRequest) {
        try {
            // Authenticate the user using AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // If authentication succeeds, generate JWT token
            String token = jwtUtility.generateToken(loginRequest.getEmail());
            System.out.print(token);
            return ResponseEntity.ok(token);
        } catch (Exception e) {

            return ResponseEntity.status(401).body("Invalid username or password");

        }
    }
}
