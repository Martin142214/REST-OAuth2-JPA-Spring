package com.example.RestOAuth2JPA.DTO.entities;

import com.example.RestOAuth2JPA.DTO.entities.patient.Address;

import jakarta.persistence.*;

@Entity
@Table(name = "user_info")
public class Personal_info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToOne(cascade = CascadeType.ALL, mappedBy = "personalInfo")
    private Patient person;

    public Patient getPerson() {
        return person;
    }

    public void setPerson(Patient person) {
        this.person = person;
    }*/

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address addressData;

    @Column(name = "phone", nullable = true)
    private String phoneNumber;

    //ЕГН
    @Column(name = "egn_person_id")
    private String verificationCode;

    public Personal_info(String firstName, String lastName, String phoneNumber, Address address, String verificationCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.addressData = address;
        this.verificationCode = verificationCode;
    }

    public Personal_info() {
        
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

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
