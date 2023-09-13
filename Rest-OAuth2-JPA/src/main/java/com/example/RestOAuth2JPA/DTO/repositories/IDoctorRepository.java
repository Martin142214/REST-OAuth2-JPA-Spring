package com.example.RestOAuth2JPA.DTO.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;

public interface IDoctorRepository extends JpaRepository<Doctor, Long> {
    
}
