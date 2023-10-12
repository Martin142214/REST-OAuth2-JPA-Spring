package com.example.RestOAuth2JPA.DTO.entities;

import java.sql.Date;

import org.hibernate.annotations.DynamicUpdate;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.enums.HealRequestStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "heal_requests")
@DynamicUpdate
public class HealRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private User patient;
    
    //many patients belong to one doctor
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private User doctor;
    
    
    @Column(name = "status", nullable = false)
    //@Enumerated(EnumType.STRING)
    private HealRequestStatus requestStatus;
    
    @Column(name = "reason")
    private String requestReason;
    
    
    @Column(name = "date", nullable = false)
    private Date requestDate;
    
    public HealRequest(User patient, User doctor, HealRequestStatus requestStatus, Date requestDate) {
        this.patient = patient;
        this.doctor = doctor;
        this.requestStatus = requestStatus;
        this.requestDate = requestDate;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getPatient() {
        return patient;
    }
    
    public void setPatient(User patient) {
        this.patient = patient;
    }
    
    public User getDoctor() {
        return doctor;
    }
    
    public void setDoctor(User doctor) {
        this.doctor = doctor;
    }
    
    public String getRequestReason() {
        return requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public HealRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(HealRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public HealRequest() {
        
    }

    public HealRequest(HealRequestStatus status) {
        this.requestStatus = status;
    }
}

