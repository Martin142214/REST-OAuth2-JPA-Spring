package com.example.RestOAuth2JPA.DTO.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.classModels.patient.Address;

public interface IAddressRepository extends JpaRepository<Address, Long> {
    
}
