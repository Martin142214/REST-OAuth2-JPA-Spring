package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/user/patient")
public class PatientUserController {
    
    @Autowired
    public PatientUserController() {
        
    }

    @GetMapping("/profile")
    public String patient_profile_page() {
        return "user_patient/patient_profile_page.html";
    }
}
