package com.example.RestOAuth2JPA.DTO.repositories.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;

public interface IUsersRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    User findByEmail(String email);
}
