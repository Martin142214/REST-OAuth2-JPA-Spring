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

import com.example.RestOAuth2JPA.DTO.classModels.AuthenticationResponse;
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

    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String register_new_user_patient() {
        return "/auth/registration_form.html";
    }
}
