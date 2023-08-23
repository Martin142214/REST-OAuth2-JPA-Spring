package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationRequest;
import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationResponse;
import com.example.RestOAuth2JPA.services.AuthService;
import com.example.RestOAuth2JPA.services.JwtUtil;
import com.example.RestOAuth2JPA.services.UserDetailsServiceImplementation;


@RestController
@RequestMapping("/api/v1")
public class ItemsRestController {

    @Autowired AuthenticationManager authenticationManager;

    @Autowired UserDetailsServiceImplementation userDetailsService;

    @Autowired JwtUtil jwtUtil;

    @Autowired AuthService _authService;

    @Autowired
    public ItemsRestController() {
        
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public String authenticate(@RequestBody AuthenticationRequest authenticationRequest, Model model) throws Exception {
        AuthenticationResponse response = _authService.authenticate(authenticationRequest);
        model.addAttribute("authToken", response.getResponseToken());
        return "login.html";
    }

    @GetMapping("/home")
    public String home() {
        return "index.html";
    }
}
