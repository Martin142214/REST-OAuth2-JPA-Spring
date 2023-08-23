package com.example.RestOAuth2JPA.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationRequest;
import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationResponse;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired UserDetailsServiceImplementation userDetailsService;

    @Autowired JwtUtil jwtUtil;

    @Autowired
    public AuthService() {
        
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws Exception {
        var username = authenticationRequest.getUsername();
        var password = authenticationRequest.getPassword();

        
        
        this.authenticate(username, password);
        
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());


        var token = jwtUtil.generateToken(userDetails);

        //Cookie cookie = new Cookie("Auth", token);
        //cookie.setMaxAge(30*6);
        //response.addCookie(cookie);

        return new AuthenticationResponse(token);
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
}
