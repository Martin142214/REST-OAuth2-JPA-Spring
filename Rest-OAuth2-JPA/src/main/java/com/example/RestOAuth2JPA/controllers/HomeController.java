package com.example.RestOAuth2JPA.controllers;

import java.net.http.HttpClient.Redirect;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.components.mappers.GrantedAuthoritiesMapper;
import com.example.RestOAuth2JPA.enums.Roles;
import com.example.RestOAuth2JPA.services.auth.UserService;

@Controller
public class HomeController {

    @Autowired UserService userService;

    private final String adminProfileUrl = "http://localhost:8080/user/admin/profile";

    private final String doctorProfileUrl = "http://localhost:8080/user/doctor/profile";

    private final String patientProfileUrl = "http://localhost:8080/user/patient/profile";

    @Autowired
    public HomeController() {
        
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home() {
        return "index.html";
    }

    @GetMapping("/user")
    public RedirectView users_url_handler_endpoint(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            var auth = GrantedAuthoritiesMapper.get_authorities_info(authentication).get("role_authorities").toString();
            var enumValue = Roles.ROLE_PATIENT.name().toString();
            int i = 0;
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            if (GrantedAuthoritiesMapper.get_authorities_info(authentication)
                                        .get("role_authorities").toString()
                                        .equals(Roles.ROLE_ADMIN.name().toString())) {

                return userService.redirectView(adminProfileUrl);
            }
            else if (GrantedAuthoritiesMapper.get_authorities_info(authentication)
                                             .get("role_authorities").toString()
                                             .equals(Roles.ROLE_DOCTOR.name().toString())) {

                return userService.redirectView(doctorProfileUrl);
            }
            else if(GrantedAuthoritiesMapper.get_authorities_info(authentication)
                                            .get("role_authorities").toString()
                                            .equals(Roles.ROLE_PATIENT.name().toString())) {
                                                
                return userService.redirectView(patientProfileUrl);
            }
            else {
                return userService.redirectView("http://localhost:8080/user/login");
            }   
        } catch (Exception e) { 
            return userService.redirectView("http://localhost:8080/user/login");
        }
        

        /*switch () {
            case "ROLE_PATIENT":
                return userService.redirectView(patientProfileUrl);
            case "ROLE_DOCTOR":
                return userService.redirectView(doctorProfileUrl);
            case "ROLE_ADMIN":
                return userService.redirectView(adminProfileUrl);
        }*/
        
    }
    
}
