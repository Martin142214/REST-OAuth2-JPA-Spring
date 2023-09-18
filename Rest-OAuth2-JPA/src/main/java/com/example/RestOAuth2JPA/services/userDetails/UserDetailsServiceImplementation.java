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

    @Autowired UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (userService.usernameExist(username)) {
            Optional<User> user = usersRepository.findByUsername(username);
            CustomUserDetails userDetails = new CustomUserDetails(user.get());
            return userDetails; 
        }
        else {
            throw new UsernameNotFoundException("The username: " + username + " is not found");
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
    
}
