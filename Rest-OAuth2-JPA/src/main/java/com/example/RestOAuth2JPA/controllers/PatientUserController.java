package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.services.auth.UserService;

@Controller
@RequestMapping(value = "/user/patient")
public class PatientUserController {

    @Autowired UserService userService;
    
    @Autowired
    public PatientUserController() {
        
    }

    @GetMapping("/profile")
    @Secured("ROLE_PATIENT")
    public ModelAndView patient_profile_page() {
        ModelAndView modelAndView = new ModelAndView("user_patient/patient_profile_page.html");
        User user = userService.get_currently_loggedInUser();
        if (user.isEnabled()) {
            modelAndView.addObject("userPatient", user);         
        }
        return modelAndView;
    }
}
