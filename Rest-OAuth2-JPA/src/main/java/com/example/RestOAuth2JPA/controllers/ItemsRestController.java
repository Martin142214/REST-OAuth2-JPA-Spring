package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationRequest;
import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationResponse;
import com.example.RestOAuth2JPA.services.JwtUtil;
import com.example.RestOAuth2JPA.services.UserDetailsServiceImplementation;


@RestController
@RequestMapping("/api/v1")
public class ItemsRestController {

    @Autowired AuthenticationManager authenticationManager;

    @Autowired UserDetailsServiceImplementation userDetailsService;

    @Autowired JwtUtil jwtUtil;

    @Autowired
    public ItemsRestController() {
        
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        var username = authenticationRequest.getUsername();
        var password = authenticationRequest.getPassword();
        
        this.authenticate(username, password);
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());


        var token = jwtUtil.generateToken(userDetails);

        //Cookie cookie = new Cookie("Auth", token);
        //cookie.setMaxAge(30*6);
        //response.addCookie(cookie);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("/home")
    public String home() {
        return "index.html";
    }
}
