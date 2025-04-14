package com.example.StudyHub.service;

import com.example.StudyHub.model.User;
import com.example.StudyHub.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;
@Service
public class CustomerUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepository.findByUserName(username).orElseThrow(()->
                new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User
                (user.getUserName(),user.getPassword(),user.getRole().stream().
                        map(role -> new SimpleGrantedAuthority(role.getRoleType())).
                        collect(Collectors.toList()));
    }
}
