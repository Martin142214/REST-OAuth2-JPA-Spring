package com.example.RestOAuth2JPA.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.classModels.AuthenticationResponse;
import com.example.RestOAuth2JPA.DTO.entities.FileDB;
import com.example.RestOAuth2JPA.DTO.entities.auth.Privilege;
import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.Address;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;
import com.example.RestOAuth2JPA.DTO.entities.patient.Personal_patient_info;
import com.example.RestOAuth2JPA.DTO.repositories.IPatientRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IAddressRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IFileDBRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IPersonalInfoRepository;
import com.example.RestOAuth2JPA.enums.Status;
import com.example.RestOAuth2JPA.services.auth.AuthService;
import com.example.RestOAuth2JPA.services.auth.PrivilegeService;
import com.example.RestOAuth2JPA.services.auth.RoleService;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.fileUpload.FileUploadUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private AuthService _authService;

    @Autowired UserService userService;

    
    @Autowired RoleService roleService;
    
    @Autowired PrivilegeService privilegeService;
    
    @Autowired IPatientRepository patientRepository;

    @Autowired IPersonalInfoRepository personalInfoRepository;

    @Autowired IAddressRepository addressRepository;

    @Autowired IFileDBRepository fileDBRepository;

    @Autowired PasswordEncoder passwordEncoder;

    public LoginController() {
        
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login.html";
    }

    @GetMapping("/admin/register")
    public String return_admin_register_form() {
        return "/auth/register_admin.html";
    }

    @PostMapping("/admin/register")
    public RedirectView processRegister(String username, String email, String password,
                                        @RequestParam("userImage") MultipartFile userImage, HttpServletRequest request) {

        /*Privilege readPrivilege = privilegeService.createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege createPrivilege = privilegeService.createPrivilegeIfNotFound("CREATE_PRIVILEGE");
        Privilege updatePrivilege = privilegeService.createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
        Privilege deletePrivilege = privilegeService.createPrivilegeIfNotFound("DELETE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, createPrivilege, updatePrivilege, deletePrivilege);
        roleService.createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        roleService.createRoleIfNotFound("ROLE_PATIENT", Arrays.asList(readPrivilege));
        roleService.createRoleIfNotFound("ROLE_DOCTOR", Arrays.asList(readPrivilege));*/

        // the above is done; saved in the DB
        
        User user = new User();
        
        Role adminRole = roleService.findByName("ROLE_ADMIN");
        if (adminRole == null) {
            throw new Error("Cannot register admin role without the role itself. Please see if the ADMIN_ROLE persist in dbo.roles");
        }

        if (username.length() >= 24) {
            throw new Error("Cannot save username with more than 24 characters");
        }
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(adminRole);
        user.setEnabled(true);
        user.setTokenExpired(false);
        user.setAdmin(true);

        //TODO add FILEDB convert and save uploaded images in the project
        FileDB userImageFile = FileUploadUtils.mkdir_and_upload_file_and_return_imagePath(username, adminRole, userImage);
        user.setProfileImage(userImageFile);

        fileDBRepository.save(userImageFile);
        this.userService.save_new_user(user);

        return redirectView("http://localhost:8080/user/register");
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String return_register_form() {
        return "/auth/registration_form.html";
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public RedirectView register_new_user_patient(@ModelAttribute User user, 
                                                   String firstName, String lastName, Integer verificationCode, 
                                                   String street, String city, Integer postCode,
                                                   @RequestParam("userImage") MultipartFile userImage) {

        if (userService.emailExist(user.getEmail())) {
            String email = user.getEmail();
            try {
                throw new Exception
                  ("There is an account with that email adress: " + email);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(userService.usernameExist(user.getUsername())){
            String username = user.getUsername();
            try {
                throw new Exception
                  ("There is an account with that username: " + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
            Address address = new Address(street, city, postCode);
            
            Personal_patient_info info = new Personal_patient_info(firstName, lastName, address, verificationCode);

            address.setPersonalInfo(info);
            
            Patient patient = new Patient(info, Status.Unknown);
            patient.setNotes(new ArrayList<>());
            info.setPerson(patient);
            
            String encodedPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPass);
            user.setEnabled(true);
            user.setRole(roleService.findByName("ROLE_PATIENT"));
            FileDB userImageFile = FileUploadUtils.mkdir_and_upload_file_and_return_imagePath(user.getUsername(), user.getRole(), userImage);
            user.setProfileImage(userImageFile);
            
            patient.setUser(user);
            int i = 0;

            fileDBRepository.save(userImageFile);
            userService.save_new_user(user);
            patientRepository.save(patient);
            personalInfoRepository.save(info);
            addressRepository.save(address); 
        } 
        catch (Exception e) {
            throw new Error(e.getMessage());
        }
        
        return redirectView("http://localhost:8080/patients");
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
