package com.example.RestOAuth2JPA.services.auth;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.IDoctorRepository;
import com.example.RestOAuth2JPA.DTO.repositories.IPatientRepository;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IAddressRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IFileDBRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IPersonalPatientInfoRepository;
import com.example.RestOAuth2JPA.components.classModels.UserModelData;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;

@Service
public class UserService {

    @Autowired RoleService roleService;

    @Autowired IUsersRepository _usersRepository;

    @Autowired IDoctorRepository doctorRepository;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired IPatientRepository patientRepository;

    @Autowired IPersonalPatientInfoRepository personalInfoRepository;

    @Autowired IAddressRepository addressRepository;

    @Autowired IFileDBRepository fileDBRepository;

    @Autowired PasswordEncoder passwordEncoder;

    public UserService() {
        
    }

    private boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    public User get_currently_loggedInUser() {

        if (authenticated()) {
            return _usersRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        else {
            return new User();
        }
    }

    public Optional<User> findById(Long id) {
        return _usersRepository.findById(id);
    }

    public User get_user_by_username(String username) {
        return _usersRepository.findByUsername(username);
    }

    public void save_new_user(User user) {
        _usersRepository.save(user);
    }

    public List<User> get_all_users() {
        return _usersRepository.findAll();
    }

    public List<User> get_all_doctor_users() {
        return _usersRepository.findAll().stream()
                                         .filter(user -> user.getRole().getName().equalsIgnoreCase("ROLE_DOCTOR"))
                                         .collect(Collectors.toList());
    }

    public List<User> get_all_patient_users() {
        return _usersRepository.findAll().stream()
                                         .filter(user -> user.getRole().getName().equalsIgnoreCase("ROLE_PATIENT"))
                                         .collect(Collectors.toList());
    }

    public boolean emailExist(String email){
        User user = _usersRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public boolean usernameExist(String username){
        User user = _usersRepository.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }

    public void create_new_user_and_saveDB(UserModelData data) {
                
        var user = data.setCompleteUserData();
        user.setRole(data.isUserDoctor ? roleService.findByName("ROLE_DOCTOR") : roleService.findByName("ROLE_PATIENT"));
        
        fileDBRepository.save(user.getProfileImage());
        if (data.isUserDoctor) {
            addressRepository.save(user.getDoctor().getPersonalInfo().getAddress()); 
            personalInfoRepository.save(user.getDoctor().getPersonalInfo());
            doctorRepository.save(user.getDoctor());
        }
        else {
            addressRepository.save(user.getPatient().getPersonalInfo().getAddress()); 
            personalInfoRepository.save(user.getPatient().getPersonalInfo());
            patientRepository.save(user.getPatient());    
        }
        this.save_new_user(user);
    }
}
