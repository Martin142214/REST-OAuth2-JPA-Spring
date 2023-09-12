package com.example.RestOAuth2JPA.DTO.classModels.patient;

import jakarta.persistence.*;

@Entity
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "personalData_id", referencedColumnName = "id")
    private PersonalInfo personalInfo;

    private String street;

    private String city;

    private Integer postCode;
    
    public Address() {}
    
    public Address(String street, String city, Integer postCode) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

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
