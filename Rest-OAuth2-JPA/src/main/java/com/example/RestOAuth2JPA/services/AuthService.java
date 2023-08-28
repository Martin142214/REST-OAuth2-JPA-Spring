package com.example.RestOAuth2JPA.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.classModels.AuthenticationRequest;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired UserDetailsServiceImplementation userDetailsService;

    @Autowired JwtUtil jwtUtil;

    @Autowired
    public AuthService() {
        
    }

    public boolean authenticate(AuthenticationRequest authenticationRequest, HttpServletRequest request) throws Exception {
        var username = authenticationRequest.getUsername();
        var password = authenticationRequest.getPassword();

        var auth = SecurityContextHolder.getContext().getAuthentication();
        
        if(this.authenticate(username, password)){
            var auth1 = SecurityContextHolder.getContext().getAuthentication();
            final UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    
            var token = jwtUtil.generateToken(userDetails);
            if (isValidTokenForAuth(username, token, request)) {
                var auth2 = SecurityContextHolder.getContext().getAuthentication();
                return true;
            }
        }
        

        //Cookie cookie = new Cookie("Auth", token);
        //cookie.setMaxAge(30*6);
        //response.addCookie(cookie);

        return false;
        //return new AuthenticationResponse(token);
    }

    private boolean authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            var auth = SecurityContextHolder.getContext().getAuthentication();
            return true;
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    public boolean isValidTokenForAuth(String username, String jwtToken, HttpServletRequest request) {
        try {
            username = jwtUtil.getUsernameFromToken(jwtToken);
        } catch (IllegalArgumentException e) {
            System.out.println("Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
            System.out.println("JWT Token has expired");
        }

        var auth = SecurityContextHolder.getContext().getAuthentication();
        //TODO the getAuthentication() method is != null beacause there is some authentication set from somewhere
        // try to find from where is set the authentication
        if (username != null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwtToken, userDetails)) {
                
                var principal = userDetails.getUsername();
                Map<String, String> credentials = new HashMap<>();
                credentials.put("username", username);
                credentials.put("password", "user");
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                var auth1 = SecurityContextHolder.getContext().getAuthentication();
                int i = 0;
            }

        }
        return true;
    }

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
