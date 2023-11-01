package com.example.RestOAuth2JPA.services.auth.Interfaces;

import java.util.Collection;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;

public interface IUserPatientService {
    ModelAndView getAllPatientUsersForDoctorAndReturnModelAndView();

    List<User> convertCollectionOfPatientEntitiesToListOfUsers(final Collection<Patient> patients);
}
