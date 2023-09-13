package com.example.RestOAuth2JPA.DTO.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;

public interface IPatientRepository extends JpaRepository<Patient, UUID> {
    
}
