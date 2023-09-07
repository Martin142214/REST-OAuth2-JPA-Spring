package com.example.RestOAuth2JPA.DTO.entities;

import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private Collection<Patient> patients;
}
