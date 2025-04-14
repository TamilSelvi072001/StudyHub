package com.example.StudyHub.repository;

import com.example.StudyHub.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <User, Long>{
    Optional<User> findByUserName(String userName);
}
