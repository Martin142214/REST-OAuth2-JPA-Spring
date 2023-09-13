package com.example.RestOAuth2JPA.DTO.entities;

import java.io.Serializable;

import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;
import com.example.RestOAuth2JPA.DTO.entities.patient.NoteData;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;

import jakarta.persistence.*;

@Entity
@Table(name = "notes")
//Many To Many table
public class Note implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    /*@JoinTable(name = "note_data",
               joinColumns = @JoinColumn(name = "note_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "noteData_id", referencedColumnName = "id")
               )*/
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "note")
    private NoteData data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public NoteData getData() {
        return data;
    }

    public void setData(NoteData data) {
        this.data = data;
    }
}
