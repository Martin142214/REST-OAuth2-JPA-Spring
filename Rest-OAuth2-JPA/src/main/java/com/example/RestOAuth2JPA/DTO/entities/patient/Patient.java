package com.example.RestOAuth2JPA.DTO.entities.patient;

import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.DTO.entities.Personal_info;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;
import com.example.RestOAuth2JPA.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "patients")
public class Patient implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personal_info_id", referencedColumnName = "id")
    private Personal_info personalInfo;

    @Enumerated(EnumType.STRING)
    @Column(name = "patient_status")
    private Status status;

    //many patients belong to one doctor

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "patient")
    private User user;

    
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "patient")
    private Collection<Note> notes;

    public Patient(Personal_info personalInfo, Status status) {
        this.personalInfo = personalInfo;
        this.status = status;
    }
    
    public Patient() {
        
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Personal_info getPersonalInfo() {
        return personalInfo;
    }
    
    public void setPersonalInfo(Personal_info personalInfo) {
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
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}
