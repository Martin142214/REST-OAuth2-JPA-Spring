package com.example.RestOAuth2JPA.services.auth;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.auth.Privilege;
import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IRoleRepository;

@Service
public class RoleService {

    @Autowired IRoleRepository roleRepository;

    public RoleService() {
        
    }

    public Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
 
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }
    
}
