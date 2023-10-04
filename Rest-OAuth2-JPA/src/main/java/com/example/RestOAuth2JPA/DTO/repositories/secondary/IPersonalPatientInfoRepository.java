package com.example.RestOAuth2JPA.DTO.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.Personal_info;

public interface IPersonalPatientInfoRepository extends JpaRepository<Personal_info, Long> {
    
}
