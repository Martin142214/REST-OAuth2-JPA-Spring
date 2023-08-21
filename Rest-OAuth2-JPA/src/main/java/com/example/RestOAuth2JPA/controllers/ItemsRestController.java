package com.example.RestOAuth2JPA.controllers;

import javax.security.sasl.AuthenticationException;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.RestOAuth2JPA.DTO.entities.UserDetailsImplement;
import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationRequest;
import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationResponse;
import com.example.RestOAuth2JPA.services.JwtUtil;
import com.example.RestOAuth2JPA.services.UserDetailsServiceImplementation;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ItemsRestController {

    @Autowired AuthenticationManager authenticationManager;

    @Autowired UserDetailsServiceImplementation userDetailsService;

    @Autowired JwtUtil jwtUtil;

    @Autowired
    public ItemsRestController() {
        
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws Exception {
        var username = authenticationRequest.getUsername();
        var password = authenticationRequest.getPassword();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            throw new Exception("Not a valid username or password");
        }
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        var token = jwtUtil.generateToken(userDetails);

        Cookie cookie = new Cookie("Auth", token);
        cookie.setMaxAge(30*6);
        response.addCookie(cookie);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @GetMapping("/home")
    public String home() {
        return "index.html";
    }
}
