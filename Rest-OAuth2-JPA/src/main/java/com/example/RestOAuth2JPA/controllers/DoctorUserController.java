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
@RequestMapping(value = "/user/doctor")
public class DoctorUserController {
    @Autowired UserService userService;
    
    @Autowired
    public DoctorUserController() {
        
    }

    @GetMapping("/profile")
    @Secured("ROLE_DOCTOR")
    public ModelAndView patient_profile_page() {
        ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_profile_page.html");
        User user = userService.get_currently_loggedInUser();
        if (user.isEnabled()) {
            modelAndView.addObject("userDoctor", user);         
        }
        return modelAndView;
    }
}
