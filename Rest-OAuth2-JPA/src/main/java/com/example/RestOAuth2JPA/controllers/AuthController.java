package com.example.RestOAuth2JPA.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.FileDB;
import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.IPatientRepository;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IAddressRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IFileDBRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IPersonalPatientInfoRepository;
import com.example.RestOAuth2JPA.components.classModels.UserModelData;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.PrivilegeService;
import com.example.RestOAuth2JPA.services.auth.RoleService;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.fileUpload.FileUploadUtils;
import com.example.RestOAuth2JPA.services.userDetails.CustomUserDetails;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    //@Autowired AuthService _authService;

    @Autowired UserService userService;

    @Autowired IUsersRepository usersRepository;
    
    @Autowired RoleService roleService;
    
    @Autowired PrivilegeService privilegeService;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired IPatientRepository patientRepository;

    @Autowired IPersonalPatientInfoRepository personalInfoRepository;

    @Autowired IAddressRepository addressRepository;

    @Autowired IFileDBRepository fileDBRepository;

    @Autowired PasswordEncoder passwordEncoder;

    private final String loginUrl = "http://localhost:8080/user/login";

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
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

        // get all users with all their parent entities data
        /*var users = userService.get_all_users().stream()
                                               .filter(u -> !u.isAdmin())
                                               .collect(Collectors.toList());*/
        
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

        return redirectHandler.redirectView("http://localhost:8080/user/register");
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String return_register_form() {
        return "/auth/registration_form.html";
    }

    /*@RequestMapping(value = "/user/patient/register", method = RequestMethod.POST)
    public RedirectView register_new_user_patient(@ModelAttribute User user, 
                                                   String firstName, String lastName, String verificationCode, 
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

            //address.setPersonalInfo(info);
            //1 info.setAddress(address);
            
            Patient patient = new Patient(info, Status.Unknown);
            patient.setNotes(new ArrayList<>());

            //info.setPerson(patient);
            //1 patient.setPersonalInfo(info);
            
            String encodedPass = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPass);
            user.setEnabled(true);
            user.setRole(roleService.findByName("ROLE_PATIENT"));
            FileDB userImageFile = FileUploadUtils.mkdir_and_upload_file_and_return_imagePath(user.getUsername(), user.getRole(), userImage);
            user.setProfileImage(userImageFile);
            
            //patient.setUser(user);
            user.setPatient(patient);
            int i = 0;

            fileDBRepository.save(userImageFile);
            addressRepository.save(address); 
            personalInfoRepository.save(info);
            patientRepository.save(patient);
            userService.save_new_user(user);
        } 
        catch (Exception e) {
            throw new Error(e.getMessage());
        }
        
        return redirectHandler.redirectView(patientLoginPageUrl);
    }*/

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public RedirectView register_new_user(String firstName, String lastName, String verificationCode, 
                                          String street, String city, Integer postCode,
                                          @RequestParam("userImage") MultipartFile userImage, Boolean isUserDoctor, 
                                          String phoneNumber, String department,String positionName,
                                          String username, String email, String password) {
        User user = new User(username, email);                                   
        //TODO change all func for doctor login, add logic needed
        if (userService.emailExist(user.getEmail())) {
            String userEmail = user.getEmail();
            try {
                throw new Exception
                  ("There is an account with that email adress: " + userEmail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if(userService.usernameExist(user.getUsername())){
            String userUsername = user.getUsername();
            try {
                throw new Exception
                  ("There is an account with that username: " + userUsername);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        try {
            String encodedPass = passwordEncoder.encode(password);
            user.setPassword(encodedPass);

            UserModelData userModelData = new UserModelData(firstName, lastName, verificationCode, street, city, postCode, userImage, isUserDoctor, phoneNumber, department, positionName, user);
            userService.create_new_user_and_saveDB(userModelData);
            int i = 0;

        } 
        catch (Exception e) {
            throw new Error(e.getMessage());
        }
        
        return redirectHandler.redirectView(loginUrl);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.GET)
    public String login_user() {
        return "/auth/login.html";

    }

    /*@RequestMapping(value = "/user/patient/login", method = RequestMethod.GET)
    public String login_user_patient() {
        return "/auth/login_user_patient.html";

    }

    @RequestMapping(value = "/user/doctor/login", method = RequestMethod.GET)
    public String login_user_doctor() {
        return "/auth/login_user_doctor.html";

    }*/

    /*@RequestMapping(value = "/user/doctor/login", method = RequestMethod.POST)
    public RedirectView login_user_doctor(String username, @RequestParam("password") String password, 
                                                   @RequestParam("accountCode") String accountCode, HttpServletRequest request) {

                CustomUserDetails user =  new CustomUserDetails(username, usersRepository);
         
                if (user == null || !user.getUsername().equalsIgnoreCase(username)) {
                    throw new BadCredentialsException("Username not found.");
                }
         
                if (!password.equals(user.getPassword())) {
                    throw new BadCredentialsException("Wrong password.");
                }

                String accountPass = userService.get_user_by_username(username).getDoctor().getAccountCode();

                if (!accountCode.equalsIgnoreCase(accountPass)) {
                    throw new BadCredentialsException("Wrong account code.");
                }

                Map<String, String> credentials = new HashMap<>();
                credentials.put("username", username);
                credentials.put("password", password);

                UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(user.getUsername(), credentials, user.getAuthorities());
                authReq.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authReq);
                
                if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                    return redirectHandler.redirectView(redirectHandler.redirect_user_handler(authReq));
                }
                else {
                    return redirectHandler.redirectView("http://localhost:8080/user/login");
                }
                //Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
         
                //return new UsernamePasswordAuthenticationToken(user, password, authorities);
        }*/
    }
