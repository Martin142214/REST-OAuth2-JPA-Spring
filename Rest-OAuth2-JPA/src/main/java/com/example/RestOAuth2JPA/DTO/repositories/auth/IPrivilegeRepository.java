package com.example.RestOAuth2JPA.DTO.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.auth.Privilege;

public interface IPrivilegeRepository extends JpaRepository<Privilege, Long> {
    
    Privilege findByName(String name);
}
