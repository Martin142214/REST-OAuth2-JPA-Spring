package com.example.RestOAuth2JPA.components.classModels.auth;

import java.util.regex.Pattern;

public class User_login {

    private String username;

    private String email;

    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (this.patternMatches(email)) {
            this.email = email;       
        }
    }

    private boolean patternMatches(String email){
        String regexExpression = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" 
        + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        return Pattern.compile(regexExpression).matcher(email).matches();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean ifAllFieldsAreNotEmpty() {
        return this.getUsername() == null || this.getEmail() == null || this.getPassword() == null ? false : true;
    }

    public User_login(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
