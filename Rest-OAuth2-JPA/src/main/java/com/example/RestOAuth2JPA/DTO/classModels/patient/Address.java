package com.example.RestOAuth2JPA.DTO.classModels.patient;

public class Address {

    private String street;

    private String city;

    private Integer postCode;

    public Address(String street, String city, Integer postCode) {
        this.street = street;
        this.city = city;
        this.postCode = postCode;
    }

    public Address() {}

    
}
