package com.example.RestOAuth2JPA.services.userDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.example.RestOAuth2JPA.DTO.entities.auth.Privilege;
import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;

// need to be a config
// @component
public class CustomUserDetails implements UserDetails {

    private User user;

    public CustomUserDetails(String username, IUsersRepository usersRepository) {
        try {
            this.user = usersRepository.findByUsername(username);
        } catch (Exception e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public CustomUserDetails() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
        //return Arrays.asList(authority);
        return getGrantedAuthorities(getPrivileges(user.getRole()));
    }

    public List<String> getPrivileges(Role role) {
 
        List<String> privileges = new ArrayList<>();
        List<Privilege> collection = new ArrayList<>();
        
        privileges.add(role.getName());
        collection.addAll(role.getPrivileges());
        for (Privilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    public List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public Role getRole() {
        return user.getRole();
    }
    
}
