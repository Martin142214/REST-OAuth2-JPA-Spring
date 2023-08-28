package com.example.RestOAuth2JPA.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.isEmpty()) {
            
            Collection<SimpleGrantedAuthority> grantedAuthorities = this.setAuthorities();
            String pass = "user";
            return new User(username.toString(), pass, grantedAuthorities);
        }
        return new User("user", "user", new ArrayList<>());
    }

    private Collection<SimpleGrantedAuthority> setAuthorities() {
        SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority("ROLE_USER");
        List<SimpleGrantedAuthority> newAuthorities = new ArrayList<>();
        newAuthorities.add(userAuthority);
        return newAuthorities;
    }
    
}
