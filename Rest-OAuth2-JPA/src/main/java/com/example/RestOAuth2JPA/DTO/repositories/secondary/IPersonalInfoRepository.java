package com.example.RestOAuth2JPA.DTO.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.patient.Personal_patient_info;

public interface IPersonalInfoRepository extends JpaRepository<Personal_patient_info, Long> {
    
}
