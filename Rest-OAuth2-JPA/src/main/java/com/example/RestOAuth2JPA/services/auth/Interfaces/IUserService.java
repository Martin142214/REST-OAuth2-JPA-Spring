package com.example.RestOAuth2JPA.services.auth.Interfaces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;

public interface IUserService {

    boolean authenticated();

    User getCurrentlyLoggedInUser();

    User getAnyUserById(Long id);

    User getUserByUsername(String username);

    Collection<User> getAllPatientUsers();

    default boolean userPatientHasDoctor(User patient) {
        return patient.getPatient().getDoctor() != null ? true : false;
    }

    default List<User> getAllPatientsForUserDoctor(User doctor) {
        List<User> doctorUserPatients = new ArrayList<>();
        for (Patient patient : doctor.getDoctor().getPatients()) {
            doctorUserPatients.add(patient.getUser());
        } 
        
        return doctorUserPatients;
    }
}
