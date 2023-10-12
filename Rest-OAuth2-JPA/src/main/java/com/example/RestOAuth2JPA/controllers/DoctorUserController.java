package com.example.RestOAuth2JPA.controllers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IHealRequestRepository;
import com.example.RestOAuth2JPA.enums.HealRequestStatus;
import com.example.RestOAuth2JPA.enums.HospitalDepartments;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.requests.HealRequestService;

@Controller
@RequestMapping(value = "/user/doctor")
public class DoctorUserController {
    
    @Autowired UserService userService;

    @Autowired HealRequestService healRequestService;

    @Autowired IHealRequestRepository healRequestRepository;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired
    public DoctorUserController() {
        
    }

    @GetMapping("/profile")
    @Secured("ROLE_DOCTOR")
    public ModelAndView doctor_profile_page() {
        ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_profile_page.html");
        User doctor = userService.get_currently_loggedInUser();
        if (doctor.isEnabled()) {
            modelAndView.addObject("userDoctor", doctor);
            List<HealRequest> allDoctorHealRequests = healRequestRepository.findAll().stream().limit(5).toList();
            modelAndView.addObject("doctorHealRequests", allDoctorHealRequests);
            modelAndView.addObject("doctorPatients", userService.getAllPatientsForDoctor(doctor));       
        }
        return modelAndView;
    }

    @GetMapping("/requests")
    @Secured("ROLE_DOCTOR")
    public ModelAndView see_all_requests(@RequestParam(name = "healRequestId", required = false) Long healRequestId) {
        User currentUser = userService.get_currently_loggedInUser();
        //TODO Make the list to return only requests from today
        List<HealRequest> allDoctorHealRequests = healRequestRepository.findAll()
                                                                       .stream().filter(req -> req.getDoctor().getId().equals(currentUser.getId()))
                                                                       .toList();
        
         ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_healRequests.html");
         modelAndView.addObject("healRequests", allDoctorHealRequests);
         modelAndView.addObject("selectedHealRequestId", healRequestId);
         return modelAndView; 

    }

    @PostMapping("/{requestId}/response")
    @Secured("ROLE_DOCTOR")
    public RedirectView response_healRequest(@PathVariable Long requestId, Long patientId, HealRequestStatus status) {
        User doctorUser = userService.get_currently_loggedInUser();
        Optional<User> patientUser = userService.getUserById(patientId);
        Optional<HealRequest> request = healRequestRepository.findById(requestId);
        if (request.isPresent()) {
            if (status.equals(HealRequestStatus.Approved) && patientUser.isPresent()) {
                doctorUser.getDoctor().addPatient(patientUser.get().getPatient());
                patientUser.get().getPatient().setDoctor(doctorUser.getDoctor());
            }
            request.get().setRequestStatus(status);
            int i = 0;

            healRequestRepository.save(request.get());
            userService.updateUser(patientUser.get());
            userService.updateUser(doctorUser);
        }
        return redirectHandler.redirectView(redirectHandler.doctorMainUrl + "requests");
    }

    @GetMapping("/patients")
    @Secured("ROLE_DOCTOR")
    public ModelAndView see_all_patients() {
        User currentUser = userService.get_currently_loggedInUser();

        Collection<Patient> allPatientsToDoctorUser = currentUser.getDoctor().getPatients();
        List<User> doctorUserPatients = new ArrayList<>();
        for (Patient patient : allPatientsToDoctorUser) {
            doctorUserPatients.add(patient.getUser());
        } 
        
         ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_patients_list.html");
         modelAndView.addObject("doctorPatients", doctorUserPatients);
         modelAndView.addObject("hasNoPatients", allPatientsToDoctorUser.size() > 0 ? false : true);
         return modelAndView; 

    }


    //Patients part

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
        if (!healRequestService.alreadyHasSuchHealRequest(doctorId, patientId)) {
            
            User currentUser = userService.get_currently_loggedInUser();
            if (currentUser.getId().equals(patientId)) {
                Optional<User> doctorUser = userService.findById(doctorId);
                if (doctorUser.isPresent()) {
                    Date date = Date.valueOf(LocalDate.now());
                    HealRequest request = new HealRequest(currentUser, doctorUser.get(), HealRequestStatus.Pending, date);
                    healRequestRepository.save(request);
                }
            } 
        }
        return redirectHandler.redirectView(redirectHandler.doctorProfileUrl + "/" + doctorId);  

    }


}
