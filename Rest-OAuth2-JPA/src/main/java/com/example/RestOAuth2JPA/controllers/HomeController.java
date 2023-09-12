package com.example.RestOAuth2JPA.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;

@Controller
public class HomeController {

    @Autowired
    public HomeController() {
        
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home() {
        return "index.html";
    }

    @GetMapping("/patients")
    public String index(){
        return "index.html";
    }
    
}
