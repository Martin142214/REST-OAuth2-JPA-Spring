package com.example.RestOAuth2JPA.DTO.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;

public interface IUsersRepository extends JpaRepository<User, Long> {
    
    User findByUsername(String username);
    
    User findByEmail(String email);
}
