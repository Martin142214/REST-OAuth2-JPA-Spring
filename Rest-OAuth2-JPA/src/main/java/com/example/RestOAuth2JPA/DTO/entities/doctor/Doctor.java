package com.example.RestOAuth2JPA.DTO.entities.doctor;

import java.util.Collection;
import java.util.UUID;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "person")
    private Personal_doctor_info personalInfo;

    //отделение пример: ортопедия
    @Column(name = "department", nullable = false)
    private String department;
    
    @Column(name = "position_name", nullable = false)
    private String positionName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;


    public Doctor() {  }

    public Doctor(Personal_doctor_info personalInfo, String department, String positionName) {
        this.personalInfo = personalInfo;
        this.department = department;
        this.positionName = positionName;
    }
    
    public String getPositionName() {
        return positionName;
    }
    
    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }
    
    public Personal_doctor_info getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(Personal_doctor_info personalInfo) {
        this.personalInfo = personalInfo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Collection<Patient> getPatients() {
        return patients;
    }

    public void setPatients(Collection<Patient> patients) {
        this.patients = patients;
    }

    //one doctor has many patients;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "doctor")
    private Collection<Patient> patients;
}