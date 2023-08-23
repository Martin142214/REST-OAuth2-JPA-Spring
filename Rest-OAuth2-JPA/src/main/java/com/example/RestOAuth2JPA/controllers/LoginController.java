package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationRequest;
import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationResponse;
import com.example.RestOAuth2JPA.services.AuthService;

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
    public String authenticate(/*@RequestBody*/ AuthenticationRequest authenticationRequest, Model model) throws Exception {
        AuthenticationResponse response = _authService.authenticate(authenticationRequest);
        model.addAttribute("authToken", response.getResponseToken());
        return "token_response.html";
    }
}
