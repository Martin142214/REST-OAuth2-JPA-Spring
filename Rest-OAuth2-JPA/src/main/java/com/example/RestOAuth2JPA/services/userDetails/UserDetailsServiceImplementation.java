package com.example.RestOAuth2JPA.services.userDetails;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.services.auth.UserService;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    @Autowired IUsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
            CustomUserDetails userDetails = new CustomUserDetails(username, usersRepository);
            
            org.springframework.security.core.userdetails.User loggedInUser = new org.springframework.security.core.userdetails.User(
                userDetails.getUsername(), 
                userDetails.getPassword(), 
                userDetails.isEnabled(), true, true, true, 
                userDetails.getAuthorities()
            );
            return loggedInUser;
    }

}
    /* 
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

    */
    

