package com.example.RestOAuth2JPA.services.auth;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

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

    public UserService() {
        
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
        else {
            return new User();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        return _usersRepository.findById(id);
    }

    @Override
    public User getUserByUsername(String username) {
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

    @Override
    public Collection<User> getAllPatientUsers() {
        //TODO check this func if works
        // check the linkedList<> shallow copy - LinkedList.clone()
        ListIterator<User> iterator = _usersRepository.findAll().listIterator();
        Collection<User> patientUsers = new LinkedList<>();
        
        while (iterator.hasNext()) {
            var currentUser = iterator.next();
            if (currentUser.getRole().getName().equalsIgnoreCase("ROLE_PATIENT")) {
                patientUsers.add(currentUser);
            }
        }
        return patientUsers;
        /*return _usersRepository.findAll().stream()
                                         .filter(user -> user.getRole().getName().equalsIgnoreCase("ROLE_PATIENT"))
                                         .collect(Collectors.toList());*/
    }

    public Optional<User> getUserById(Long id) {
        return _usersRepository.findById(id);
    }

    public void updateUser(User user) {
        _usersRepository.save(user);
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
    
    
    //METHODS FOR PATIENT USERS

    @Override
    public List<User> convertCollectionOfPatientEntitiesToListOfUsers(final Collection<Patient> patients) {
        //TODO CHECK how can I map all patient entities to their users without iteratng all the database entitites
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
        for (int i = 0; i < this.get_all_doctor_users().size(); i++) {
            var doctorEntity = this.get_all_doctor_users().get(i).getDoctor();
            doctorUsers.put(this.get_all_doctor_users().get(i).getId(), doctorEntity.getPersonalInfo().getFirstName() + " " +
                                                                               doctorEntity.getPersonalInfo().getLastName() + ", " + 
                                                                               doctorEntity.getPositionName());      
        }
        return doctorUsers;
    }



    

}
