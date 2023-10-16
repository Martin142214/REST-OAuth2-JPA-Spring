package com.example.RestOAuth2JPA.components.classModels.auth;

public class User_login {

    public String username;

    public String email;

    public String password;

    public User_login(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
