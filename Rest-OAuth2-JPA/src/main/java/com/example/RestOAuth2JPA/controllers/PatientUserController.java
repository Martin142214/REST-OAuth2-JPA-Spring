package com.example.RestOAuth2JPA.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.RestOAuth2JPA.DTO.entities.HealRequest;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.enums.HealRequestStatus;
import com.example.RestOAuth2JPA.enums.HospitalDepartments;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.fileUpload.FileUploadUtils;

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
        User patient = userService.get_currently_loggedInUser();
        Map<Long, String> doctorUsers = new HashMap<>();
        for (int i = 0; i < userService.get_all_doctor_users().size(); i++) {
            var doctorEntity = userService.get_all_doctor_users().get(i).getDoctor();
            doctorUsers.put(userService.get_all_doctor_users().get(i).getId(), doctorEntity.getPersonalInfo().getFirstName() + " " +
                                                                               doctorEntity.getPersonalInfo().getLastName() + ", " + 
                                                                               doctorEntity.getPositionName());      
        }
        if (patient.isEnabled()) {
            modelAndView.addObject("userPatient", patient); 
            modelAndView.addObject("doctorUsers", doctorUsers); 
            if (userService.patientHasDoctor(patient)) {
                modelAndView.addObject("patientHasDoctor", userService.patientHasDoctor(patient));
                modelAndView.addObject("patientDoctor", patient.getPatient().getDoctor().getUser());
            }       
        }
        return modelAndView;
    }

    @GetMapping("/profile/{id}")
    @Secured("ROLE_DOCTOR")
    public ModelAndView get_patient_profile_page(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("user_patient/patient_access_page.html");
        Optional<User> userPatient = userService.findById(id);
        if (userPatient.isPresent()) {
            modelAndView.addObject("patientInfo", userPatient.get());
            /*modelAndView.addObject("patientId", userService.get_currently_loggedInUser().getId()); 
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
            }*/
        }
        return modelAndView;
    }
}
