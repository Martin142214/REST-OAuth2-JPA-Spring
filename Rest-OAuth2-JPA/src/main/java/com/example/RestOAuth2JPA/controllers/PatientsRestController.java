package com.example.RestOAuth2JPA.controllers;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.RestOAuth2JPA.DTO.classModels.AuthenticationResponse;
import com.example.RestOAuth2JPA.DTO.classModels.patient.Address;
import com.example.RestOAuth2JPA.DTO.classModels.patient.PersonalInfo;
import com.example.RestOAuth2JPA.DTO.entities.Patient;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.IUsersRepository;
import com.example.RestOAuth2JPA.enums.Status;
import com.example.RestOAuth2JPA.services.AuthService;
import com.example.RestOAuth2JPA.services.JwtUtil;
import com.example.RestOAuth2JPA.services.UserDetailsServiceImplementation;


@RestController
@RequestMapping("/api/v1")
public class PatientsRestController {

    @Autowired AuthenticationManager authenticationManager;

    @Autowired UserDetailsServiceImplementation userDetailsService;

    @Autowired JwtUtil jwtUtil;

    @Autowired AuthService _authService;

    @Autowired IUsersRepository usersRepository;

    @Autowired
    public PatientsRestController() {
        
    }

    @GetMapping("/patients")
    public Collection<User> index(){
        return usersRepository.findAll();
    }

    @GetMapping("/patient/{id}")
    public User show(@PathVariable String id){
        UUID blogId = UUID.fromString(id);
        return usersRepository.findById(blogId).get();
    }

    @PostMapping("/user/create")
    public User create(@RequestBody String username, String password, String email, 
                                    String firstName, String lastName, Integer verificationCode, 
                                    String street, String city, Integer postCode){

        Address address = new Address(street, city, postCode);
        PersonalInfo personalInfo = new PersonalInfo(firstName, lastName, address, verificationCode);
        Patient patient = new Patient(personalInfo, Status.Unknown);
        User user = new User(username, password, email, null, patient);
        return usersRepository.save(user);
    }
}
