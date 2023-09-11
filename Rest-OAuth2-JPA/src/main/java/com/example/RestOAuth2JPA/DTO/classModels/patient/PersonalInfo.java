package com.example.RestOAuth2JPA.DTO.classModels.patient;

import com.example.RestOAuth2JPA.DTO.entities.Patient;

import jakarta.persistence.*;

@Entity
public class PersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient person;

    private String firstName;

    private String lastName;

    private Address address;

    //ЕГН
    private Integer verificationCode;

    public PersonalInfo(String firstName, String lastName, Address address, Integer verificationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Integer getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(Integer verificationCode) {
        this.verificationCode = verificationCode;
    }

    public PersonalInfo() {
        
    }
}
