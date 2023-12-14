package com.example.RestOAuth2JPA.controllers;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.HealRequest;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.enums.HealRequestStatus;
import com.example.RestOAuth2JPA.enums.HospitalDepartments;
import com.example.RestOAuth2JPA.services.additional.DataAccessUtil;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.requests.HealRequestService;

@Controller
@RequestMapping(value = "/user/doctor")
public class DoctorUserController {
    
    @Autowired UserService userService;

    @Autowired HealRequestService healRequestService;

    @Autowired DataAccessUtil dataAccessUtil;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired
    public DoctorUserController() {
        
    }

    @GetMapping("/profile")
    @Secured("ROLE_DOCTOR")
    public ModelAndView doctor_profile_page() {
        ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_profile_page.html");
        User doctor = userService.getCurrentlyLoggedInUser();
        if (doctor.isEnabled()) {
            modelAndView.addObject("userDoctor", doctor);
            List<HealRequest> allDoctorHealRequests = dataAccessUtil.getHealRequestsInDB()
                                                                    .stream()
                                                                    .filter(req -> req.getDoctor().getId().equals(doctor.getId()))
                                                                    .limit(5)
                                                                    .toList();

            modelAndView.addObject("doctorHealRequests", allDoctorHealRequests);
            modelAndView.addObject("doctorPatients", userService.getAllPatientsForUserDoctor(doctor));       
        }
        return modelAndView;
    }

    @GetMapping("/requests")
    @Secured("ROLE_DOCTOR")
    public ModelAndView see_all_requests(@RequestParam(name = "healRequestId", required = false) Long healRequestId) {
        User currentUser = userService.getCurrentlyLoggedInUser();
        //TODO Make the list to return only requests from today
        List<HealRequest> allDoctorHealRequests = dataAccessUtil.getHealRequestsInDB()
                                                                .stream()
                                                                .filter(req -> req.getDoctor().getId().equals(currentUser.getId()))
                                                                .toList();
        
         ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_healRequests.html");
         modelAndView.addObject("healRequests", allDoctorHealRequests);
         modelAndView.addObject("selectedHealRequestId", healRequestId);
         return modelAndView; 

    }

    @PostMapping("/{requestId}/response")
    @Secured("ROLE_DOCTOR")
    public RedirectView response_healRequest(@PathVariable Long requestId, Long patientId, HealRequestStatus status) {

        healRequestService.setHealRequestStatusAndSaveUsersInDB(patientId, requestId, status);
        return redirectHandler.redirectView(redirectHandler.doctorMainUrl + "requests");
    }

    @GetMapping("/patients")
    @Secured("ROLE_DOCTOR")
    public ModelAndView see_all_patients() {
         return userService.getAllPatientUsersForDoctorAndReturnModelAndView(); 
    }


    //Patients part

    @GetMapping("/profile/{id}")
    @Secured("ROLE_PATIENT")
    public ModelAndView get_doctor_profile_page(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_access_page.html");
        User userDoctor = userService.getAnyUserById(id);
            
            if (dataAccessUtil.getHealRequestsInDB().size() != 0) {
                Optional<HealRequest> request = dataAccessUtil.getHealRequestsInDB()
                                                              .stream()
                                                              .filter(req -> req.getDoctor().getId().equals(id) 
                                                                          && req.getPatient().getId().equals(userService.getCurrentlyLoggedInUser().getId()))
                                                              .findAny(); 
                
                modelAndView.addObject("request", request.get()); 
            } 
            else {
                modelAndView.addObject("request", new HealRequest(HealRequestStatus.Request));
            }

            var doctorDepartment = HospitalDepartments.valueOf(userDoctor.getDoctor().getDepartment()).getDepartmentName();  
            modelAndView.addObject("department", doctorDepartment);
            modelAndView.addObject("doctorInfo", userDoctor);
            modelAndView.addObject("patientId", userService.getCurrentlyLoggedInUser().getId()); 

        return modelAndView;
    }

    @PostMapping("/{doctorId}/request")
    @Secured("ROLE_PATIENT")
    public RedirectView send_healRequest(@PathVariable Long doctorId, Long patientId) {
        if (!healRequestService.alreadyHasSuchHealRequest(doctorId, patientId)) {
            
            User currentUser = userService.getCurrentlyLoggedInUser();
            if (currentUser.getId().equals(patientId)) {
                User doctorUser = userService.getAnyUserById(doctorId);
                    
                Date date = Date.valueOf(LocalDate.now());
                HealRequest request = new HealRequest(currentUser, doctorUser, HealRequestStatus.Pending, date);
                healRequestService.update(request);
                
            } 
        }
        return redirectHandler.redirectView(redirectHandler.doctorProfileUrl + "/" + doctorId);  

    }


}
