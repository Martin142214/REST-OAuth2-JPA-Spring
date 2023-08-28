package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationRequest;
import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationResponse;
import com.example.RestOAuth2JPA.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private AuthService _authService;

    public LoginController() {
        
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login.html";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public RedirectView authenticate(/*@RequestBody*/ AuthenticationRequest authenticationRequest, HttpServletRequest request) throws Exception {
        boolean isAuthenticated = _authService.authenticate(authenticationRequest, request);
        if (isAuthenticated) {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return _authService.redirectView("http://localhost:8080/home");
        }
        else {
            return _authService.redirectView("http://localhost:8080/login");
        }
    }
}
