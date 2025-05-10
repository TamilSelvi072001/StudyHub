package com.example.StudyHub.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RegisterRequest {
    private String email;
    private String phone;
    private String dob;
    private Set<String> roles;
    private String userName;
    private String password;
}