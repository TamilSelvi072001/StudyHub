package com.example.StudyHub.dto;

import com.example.StudyHub.model.Role;
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
    private String fullName;
    private String email;
    private Set<String> roles;
    private String userName;
    private String password;


}