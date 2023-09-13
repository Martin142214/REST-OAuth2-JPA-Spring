package com.example.RestOAuth2JPA.DTO.repositories.auth;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;

public interface IUsersRepository extends JpaRepository<User, UUID> {
    
    User findByUsername(String username);
    
    User findByEmail(String email);
}
