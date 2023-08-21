package com.example.RestOAuth2JPA.DTO.entities.classModels;

public class AuthenticationResponse {
    private String responseToken;

    public String getResponseToken() {
        return responseToken;
    }

    public AuthenticationResponse(String jwt) {
        this.responseToken = jwt;
    }

    
}
