package com.example.RestOAuth2JPA.services.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;
import com.example.RestOAuth2JPA.DTO.repositories.IDoctorRepository;
import com.example.RestOAuth2JPA.DTO.repositories.IPatientRepository;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IAddressRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IFileDBRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IPersonalPatientInfoRepository;
import com.example.RestOAuth2JPA.components.classModels.UserModelData;
import com.example.RestOAuth2JPA.components.classModels.auth.User_login;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.Interfaces.IUserDoctorService;
import com.example.RestOAuth2JPA.services.auth.Interfaces.IUserPatientService;
import com.example.RestOAuth2JPA.services.auth.Interfaces.IUserService;

@Service
public class UserService implements IUserService, IUserPatientService, IUserDoctorService {

    @Autowired RoleService roleService;

    @Autowired IUsersRepository _usersRepository;

    @Autowired IDoctorRepository doctorRepository;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired IPatientRepository patientRepository;

    @Autowired IPersonalPatientInfoRepository personalInfoRepository;

    @Autowired IAddressRepository addressRepository;

    @Autowired IFileDBRepository fileDBRepository;

    @Autowired PasswordEncoder passwordEncoder;

    private Collection<User> allUpToDateUsersInDB;

    private User currentlyLoggedInUser;

    @Autowired
    public UserService(IUsersRepository usersRepository) {
        this._usersRepository = usersRepository;
        this.allUpToDateUsersInDB = _usersRepository.findAll();
    }

    public User getCurrentUser() {
        return this.currentlyLoggedInUser;
    }

    public void setCurrentUser(User user) {
        if (user != null) {
            this.currentlyLoggedInUser = user;
        }
    }

    // METHODS FOR GENERAL USE
    @Override
    public boolean authenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        return authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken);
    }

    @Override
    public User getCurrentlyLoggedInUser() {

        if (authenticated()) {
            return _usersRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return null;
        
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(Long id) {
        return _usersRepository.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
        return _usersRepository.findByUsername(username);
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void saveNewOrUpdateUser(final User user) throws SQLException {
        if (user != null) {
            _usersRepository.save(user);    
        }
    }

    @Transactional(rollbackFor = {SQLException.class})
    public void deleteUser(final Long id) throws SQLException {
        Optional<User> user = this.findById(id);
        if (user.isPresent()) {
            _usersRepository.delete(user.get());
        }
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return _usersRepository.findAll();
    }

    public List<User> filterUsersByRole(final String role) {
        if (!this.allUpToDateUsersInDB.isEmpty()) {
            return this.allUpToDateUsersInDB.stream()
                                            .filter(user -> user.getRole().getName().equalsIgnoreCase(role))
                                            .collect(Collectors.toList());  
        }
        return new ArrayList<User>();
    }

    public List<User> getAllDoctorUsers() {
        return this.filterUsersByRole("ROLE_DOCTOR");
    }

    @Override
    public List<User> getAllPatientUsers() {
        return this.filterUsersByRole("ROLE_PATIENT");
    }

    public boolean emailExist(String email){
        User user = _usersRepository.findByEmail(email);
        return user != null ? true : false;
        
    }

    public boolean usernameExist(String username){
        User user = _usersRepository.findByUsername(username);
        return user != null ? true : false;
    }

    /**
     * print an collection of generic items
     * @param collection Collection<T> to be printed
     * @throws IndexOutOfBoundException
     */

    /*public <T> void print(final Collection<T> collection) {
        for (final T elemenT : collection) {
            Arrays.stream(collection.toArray()).forEach(element -> {
                var el = element.toString();
            });
        }
    }
        */
    public void outputStream() {
        final Path path = new File("/ss/").toPath();
        try (ObjectOutputStream oStream = new ObjectOutputStream(Files.newOutputStream(path))) {
            Doctor doctor = new Doctor();
            oStream.writeObject(doctor);
        } catch (Exception e) {
            // TODO: handle exception
        }
        
        try (ObjectInputStream inputStream = new ObjectInputStream(Files.newInputStream(path))) {
            Doctor doctor = (Doctor) inputStream.readObject();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void createNewUserAndSaveDB(UserModelData data) throws Exception {
                
        var user = data.setCompleteUserData();
        user.setRole(data.isUserDoctor ? roleService.findByName("ROLE_DOCTOR") : roleService.findByName("ROLE_PATIENT"));
        
        try {
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

            this.saveNewOrUpdateUser(user);

        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        finally {
            this.allUpToDateUsersInDB = this.getAllUsers();
        }

    }

    //METHODS FOR PATIENT USERS

    public User createAdminUserAndReturn(final User_login userLoginData, Role adminRole) {
        
        if (adminRole == null) {
            throw new Error("Cannot register admin role without the role itself. Please see if the ADMIN_ROLE persist in dbo.roles");
        }
        
        if (userLoginData.getUsername().length() >= 24) {
            throw new Error("Cannot save username with more than 24 characters");
        }

        User user = new User();
        user.setUsername(userLoginData.getUsername());
        user.setPassword(passwordEncoder.encode(userLoginData.getPassword()));
        user.setEmail(userLoginData.getEmail());
        user.setRole(adminRole);
        user.setEnabled(true);
        user.setTokenExpired(false);
        user.setAdmin(true);

        return user;
    }
    
    
    //METHODS FOR PATIENT USERS

    @Override
    public List<User> convertCollectionOfPatientEntitiesToListOfUsers(final Collection<Patient> patients) {
        List<User> userPatients = new ArrayList<>();
        for (Patient patient : patients) {
            userPatients.add(patient.getUser());
        } 
        return userPatients;
    }

    @Override
    public ModelAndView getAllPatientUsersForDoctorAndReturnModelAndView() {

        Collection<Patient> allPatientsToDoctorUser = this.getCurrentlyLoggedInUser().getDoctor().getPatients();
        List<User> doctorUserPatients = this.convertCollectionOfPatientEntitiesToListOfUsers(allPatientsToDoctorUser);

         ModelAndView modelAndView = new ModelAndView("user_doctor/doctor_patients_list.html");
         modelAndView.addObject("doctorPatients", doctorUserPatients);
         modelAndView.addObject("hasNoPatients", allPatientsToDoctorUser.size() > 0 ? false : true);
         return modelAndView;
    }

    //METHODS FOR DOCTOR USERS

    @Override
    public Map<Long, String> addDoctorInfoDataInHashMap() {
        Map<Long, String> doctorUsers = new HashMap<>();
        for (int i = 0; i < this.getAllDoctorUsers().size(); i++) {
            var doctorEntity = this.getAllDoctorUsers().get(i).getDoctor();
            doctorUsers.put(this.getAllDoctorUsers().get(i).getId(), doctorEntity.getPersonalInfo().getFirstName() + " " +
                                                                               doctorEntity.getPersonalInfo().getLastName() + ", " + 
                                                                               doctorEntity.getPositionName());      
        }
        return doctorUsers;
    }



    

}
