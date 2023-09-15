package com.example.RestOAuth2JPA.DTO.entities.patient;

import jakarta.persistence.*;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToOne(cascade = CascadeType.ALL, mappedBy = "addressData")
    private Personal_patient_info personalInfo;*/

    private String street;

    private String city;

    private Integer postCode;
    
    public Address() {}
    
    public Address(String street, String city, Integer postCode) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
    }

    /*public Personal_patient_info getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(Personal_patient_info personalInfo) {
        this.personalInfo = personalInfo;
    }*/

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }


    
}
