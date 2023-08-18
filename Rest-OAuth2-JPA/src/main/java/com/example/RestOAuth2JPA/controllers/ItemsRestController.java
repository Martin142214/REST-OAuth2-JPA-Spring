package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/home")
public class ItemsRestController {

    @Autowired
    public ItemsRestController() {
        
    }

    @GetMapping
    public String home() {
        return "index.html";
    }
}
