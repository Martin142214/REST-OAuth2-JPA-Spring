package com.example.RestOAuth2JPA.DTO.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.classModels.patient.PersonalInfo;

public interface IPersonalInfoRepository extends JpaRepository<PersonalInfo, Long> {
    
}
