package com.example.RestOAuth2JPA.DTO.repositories.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.auth.Role;

public interface IRoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
