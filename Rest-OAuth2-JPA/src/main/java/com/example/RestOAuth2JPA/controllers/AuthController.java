package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.IPatientRepository;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IAddressRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IPersonalPatientInfoRepository;
import com.example.RestOAuth2JPA.components.classModels.UserModelData;
import com.example.RestOAuth2JPA.components.classModels.auth.User_login;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.AuthService;
import com.example.RestOAuth2JPA.services.auth.PrivilegeService;
import com.example.RestOAuth2JPA.services.auth.RoleService;
import com.example.RestOAuth2JPA.services.auth.UserService;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @Autowired UserService userService;

    @Autowired AuthService authService;
    
    @Autowired RoleService roleService;
    
    @Autowired PrivilegeService privilegeService;

    @Autowired RedirectHandler redirectHandler;
    
    @Autowired IPatientRepository patientRepository;

    @Autowired IPersonalPatientInfoRepository personalInfoRepository;

    @Autowired IAddressRepository addressRepository;


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
        
        User_login userLoginData = new User_login(username, password);
        userLoginData.setEmail(email);
        authService.createNewAdminUserAndSaveToDB(userLoginData, userImage);

        return redirectHandler.redirectView("http://localhost:8080/user/login");
    }

    @RequestMapping(value = "/user/register", method = RequestMethod.GET)
    public String return_register_form() {
        return "/auth/registration_form.html";
    }

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
            userService.createNewUserAndSaveDB(userModelData);
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
