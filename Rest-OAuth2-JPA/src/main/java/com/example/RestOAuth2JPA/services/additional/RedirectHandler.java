package com.example.RestOAuth2JPA.services.additional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.components.mappers.GrantedAuthoritiesMapper;
import com.example.RestOAuth2JPA.enums.Roles;

@Service
public class RedirectHandler {

    private final String adminProfileUrl = "http://localhost:8080/user/admin/profile";

    private final String doctorProfileUrl = "http://localhost:8080/user/doctor/profile";

    private final String patientProfileUrl = "http://localhost:8080/user/patient/profile";


    public RedirectHandler() {
        
    }

    public String redirect_user_handler(Authentication authentication) {

        try {
            var roleAuthority = GrantedAuthoritiesMapper.get_authorities_info(authentication).get("role_authorities").toString();

            if (roleAuthority.equals(Roles.ROLE_ADMIN.name().toString())) {
                return adminProfileUrl;
            }
            else if (roleAuthority.equals(Roles.ROLE_DOCTOR.name().toString())) {
                return doctorProfileUrl;
            }
            else if(roleAuthority.equals(Roles.ROLE_PATIENT.name().toString())) {                                        
                return patientProfileUrl;
            }
            else {
                return "http://localhost:8080/user/login";
            }   
        } catch (Exception e) { 
            return e.getMessage();
        }
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
