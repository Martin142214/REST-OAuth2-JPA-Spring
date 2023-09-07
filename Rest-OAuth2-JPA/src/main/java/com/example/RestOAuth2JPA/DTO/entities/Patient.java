package com.example.RestOAuth2JPA.DTO.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import com.example.RestOAuth2JPA.DTO.classModels.patient.Address;
import com.example.RestOAuth2JPA.DTO.classModels.patient.PersonalInfo;
import com.example.RestOAuth2JPA.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "PATIENTS")
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private PersonalInfo personalInfo;

    private Status status;

    //many patients belong to one doctor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "patient")
    private Collection<Note> notes;

    public Patient(PersonalInfo personalInfo, Status status) {
        this.personalInfo = personalInfo;
        this.status = status;
    }

    public Patient() {
        
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Collection<Note> getNotes() {
        return notes;
    }

    public void setNotes(Collection<Note> notes) {
        this.notes = notes;
    }
}
