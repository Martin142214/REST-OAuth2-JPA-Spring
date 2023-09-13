package com.example.RestOAuth2JPA.DTO.classModels.patient;

import com.example.RestOAuth2JPA.DTO.entities.Patient;

import jakarta.persistence.*;

@Entity
@Table(name = "personal_info")
public class PersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient person;

    public Patient getPerson() {
        return person;
    }

    public void setPerson(Patient person) {
        this.person = person;
    }

    @Column(name = "patient_firstname")
    private String firstName;

    @Column(name = "patient_lastname")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "personalInfo")
    private Address addressData;

    //ЕГН
    @Column(name = "egn_person_id")
    private Integer verificationCode;

    public PersonalInfo(String firstName, String lastName, Address address, Integer verificationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.addressData = address;
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
        return addressData;
    }

    public void setAddress(Address address) {
        this.addressData = address;
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
