package com.example.RestOAuth2JPA.DTO.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.RestOAuth2JPA.DTO.entities.Patient;

public interface IPatientsRepository extends JpaRepository<Patient, String> {
    
}
