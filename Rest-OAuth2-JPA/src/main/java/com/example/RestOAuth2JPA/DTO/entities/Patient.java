package com.example.RestOAuth2JPA.DTO.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import com.example.RestOAuth2JPA.DTO.classModels.patient.Address;
import com.example.RestOAuth2JPA.DTO.classModels.patient.PersonalInfo;
import com.example.RestOAuth2JPA.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private PersonalInfo personalInfo;

    private String userName;

    private Status status;

    private Doctor doctor;

    private Collection<Note> notes;
}
