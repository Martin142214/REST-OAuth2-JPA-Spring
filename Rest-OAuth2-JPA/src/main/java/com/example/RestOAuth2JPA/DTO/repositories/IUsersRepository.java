package com.example.RestOAuth2JPA.DTO.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;

public interface IUsersRepository extends JpaRepository<User, UUID> {
    
}
