package com.example.RestOAuth2JPA.controllers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.HealRequest;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IHealRequestRepository;
import com.example.RestOAuth2JPA.enums.HealRequestStatus;
import com.example.RestOAuth2JPA.enums.HospitalDepartments;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.UserService;

@Controller
@RequestMapping(value = "/user/doctor")
public class DoctorUserController {
    
    @Autowired UserService userService;

    @Autowired IHealRequestRepository healRequestRepository;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired
    public DoctorUserController() {
        
    }

    @GetMapping("/profile")
    @Secured("ROLE_DOCTOR")
    public ModelAndView doctor_profile_page() {
        ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_profile_page.html");
        User user = userService.get_currently_loggedInUser();
        if (user.isEnabled()) {
            modelAndView.addObject("userDoctor", user);         
        }
        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    @Secured("ROLE_PATIENT")
    public ModelAndView get_doctor_profile_page(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_access_page.html");
        Optional<User> userDoctor = userService.findById(id);
        if (userDoctor.isPresent()) {
            modelAndView.addObject("doctorInfo", userDoctor.get());
            modelAndView.addObject("patientId", userService.get_currently_loggedInUser().getId()); 
            var doctorDepartment = HospitalDepartments.valueOf(userDoctor.get().getDoctor().getDepartment()).getDepartmentName();   
            modelAndView.addObject("department", doctorDepartment);
            if (healRequestRepository.count() != 0) {
                Optional<HealRequest> request = healRequestRepository.findAll().stream()
                                                                     .filter(req -> req.getDoctor().getId().equals(id) 
                                                                                 && req.getPatient().getId().equals(userService.get_currently_loggedInUser()
                                                                                                                               .getId())).findAny(); 
                modelAndView.addObject("request", request.get()); 
            } 
            else {
                modelAndView.addObject("request", new HealRequest(HealRequestStatus.Request));
            }
        }
        return modelAndView;
    }

    @PostMapping("/{doctorId}/request")
    @Secured("ROLE_PATIENT")
    public RedirectView send_healRequest(@PathVariable Long doctorId, Long patientId) {
        User currentUser = userService.get_currently_loggedInUser();
        if (currentUser.getId().equals(patientId)) {
            Optional<User> doctorUser = userService.findById(doctorId);
            if (doctorUser.isPresent()) {
                Date date = Date.valueOf(LocalDate.now());
                HealRequest request = new HealRequest(currentUser, doctorUser.get(), HealRequestStatus.Pending, date);
                healRequestRepository.save(request);
            }
        }
        return redirectHandler.redirectView(redirectHandler.doctorProfileUrl + "/" + doctorId);  

    }
}
