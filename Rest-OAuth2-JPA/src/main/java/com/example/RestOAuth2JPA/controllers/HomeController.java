package com.example.RestOAuth2JPA.controllers;

import java.net.http.HttpClient.Redirect;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.components.mappers.GrantedAuthoritiesMapper;
import com.example.RestOAuth2JPA.enums.Roles;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.UserService;

@Controller
@ControllerAdvice
public class HomeController {

    @Autowired UserService userService;

    @Autowired RedirectHandler redirectHandler;

    //private static final Authentication AUTHENTICATION = SecurityContextHolder.getContext().getAuthentication();

    @Autowired
    public HomeController() {
        
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home() {
        return "index.html";
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        this.userService.setCurrentUser(userService.getCurrentlyLoggedInUser());
        model.addAttribute("currentUser", userService.getCurrentUser());
    }

    @GetMapping("/user/checkPath")
    public RedirectView usersUrlHandlerEndpoint(){
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            var auth = GrantedAuthoritiesMapper.get_authorities_info(authentication).get("role_authorities").toString();
            var enumValue = Roles.ROLE_PATIENT.name().toString();
            int i = 0;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return redirectHandler.redirectView(redirectHandler.redirect_user_handler(authentication));
    }
    
}
