package com.example.RestOAuth2JPA.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.RestOAuth2JPA.DTO.entities.UserDetailsImplement;

public class UserDetailsServiceImplementation implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserDetailsImplement("user");
    }
    
}
