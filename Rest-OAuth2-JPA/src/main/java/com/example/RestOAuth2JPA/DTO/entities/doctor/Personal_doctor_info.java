package com.example.RestOAuth2JPA.DTO.entities.doctor;

import jakarta.persistence.*;

@Entity
@Table(name = "doctor_info")
public class Personal_doctor_info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personalInfo")
    private Doctor person;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "phone", nullable = true)
    private Integer phoneNumber;

    //ЕГН
    @Column(name = "egn_person_id")
    private Integer verificationCode;

    public Personal_doctor_info() {
        
    }

    public Personal_doctor_info(String firstName, String lastName, Integer phoneNumber, Integer verificationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.verificationCode = verificationCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Doctor getPerson() {
        return person;
    }

    public void setPerson(Doctor person) {
        this.person = person;
    }
}
