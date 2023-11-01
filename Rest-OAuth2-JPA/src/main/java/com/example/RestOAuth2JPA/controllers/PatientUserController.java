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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.HealRequest;
import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.NoteData;
import com.example.RestOAuth2JPA.DTO.repositories.INotesRepository;
import com.example.RestOAuth2JPA.enums.HealRequestStatus;
import com.example.RestOAuth2JPA.enums.HospitalDepartments;
import com.example.RestOAuth2JPA.enums.Status;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.fileUpload.FileUploadUtils;

@Controller
@RequestMapping(value = "/user/patient")
public class PatientUserController {

    @Autowired UserService userService;

    @Autowired INotesRepository notesRepository;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired
    public PatientUserController() {
        
    }

    @PostMapping("/profile/{id}/setStatus")
    @Secured("ROLE_DOCTOR")
    public RedirectView setStatusToPatient(@PathVariable Long id, String status) {
        Optional<User> userPatient = userService.findById(id);
        if (userPatient.isPresent()) {
            userPatient.get().getPatient().setStatus(Status.valueOf(status));
        }
        
        return redirectHandler.redirectView(redirectHandler.patientProfileUrl + id);
    }

    @GetMapping("/profile")
    @Secured("ROLE_PATIENT")
    public ModelAndView patient_profile_page() {
        User patient = userService.getCurrentlyLoggedInUser();
        
        ModelAndView modelAndView = new ModelAndView("user_patient/patient_profile_page.html");
        modelAndView.addObject("doctorUsers", userService.addDoctorInfoDataInHashMap()); 
        if (userService.userPatientHasDoctor(patient)) {
            modelAndView.addObject("patientHasDoctor", userService.userPatientHasDoctor(patient));
            modelAndView.addObject("patientDoctor", patient.getPatient().getDoctor().getUser());
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
            modelAndView.addObject("doctorUser", userService.getCurrentlyLoggedInUser());
            var notesForPatient = notesRepository.findAll().stream().filter(note -> note.getDoctor().getId().equals(userService.getCurrentlyLoggedInUser().getDoctor().getId()) &&
                                                                                    note.getPatient().getId().equals(userPatient.get().getPatient().getId())).toList();
            int i = 0; 
            //TODO have stackoverflow exception because of many relational data in notes entity (patient, doctor)
            /*Map<Long, NoteData> patientNotes = new HashMap<>();
            for (Note note : notesForPatient) {
                patientNotes.put(note.getId(), note.getData());
            }*/
            if (!notesForPatient.isEmpty()) {
                String patientName = userPatient.get().getPatient().getPersonalInfo().getFirstName() + " " + userPatient.get().getPatient().getPersonalInfo().getLastName();
                modelAndView.addObject("patientFullName", patientName);
                modelAndView.addObject("patientNotes", notesForPatient);       
            }
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
