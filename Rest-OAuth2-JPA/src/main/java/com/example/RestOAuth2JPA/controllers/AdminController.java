package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(value = "/user/admin")
public class AdminController {

    @Autowired
    public AdminController() {
        
    }

    @GetMapping("/profile")
    public String profile_page() {
        return "admin/profile_page.html";
    }

    
}
