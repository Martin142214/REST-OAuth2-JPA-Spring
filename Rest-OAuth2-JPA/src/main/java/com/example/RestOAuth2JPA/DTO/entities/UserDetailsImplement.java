package com.example.RestOAuth2JPA.DTO.entities;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDetailsImplement implements UserDetails {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String username;

    private String password;

    private List<GrantedAuthority> authorities;

    public UserDetailsImplement(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // TODO Auto-generated method stub
        return authorities;
        
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        var pass = passwordEncoder.encode(password);
        password = pass;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
