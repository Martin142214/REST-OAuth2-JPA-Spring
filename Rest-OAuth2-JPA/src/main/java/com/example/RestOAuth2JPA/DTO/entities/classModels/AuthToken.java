package com.example.RestOAuth2JPA.DTO.entities.classModels;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "auth")
@Configuration("application")
@Component
public class AuthToken {

    private String secret_key;

    //Getters and Setters go here
    
    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

}
