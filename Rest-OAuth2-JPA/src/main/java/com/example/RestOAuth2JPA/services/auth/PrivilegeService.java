package com.example.RestOAuth2JPA.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.auth.Privilege;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IPrivilegeRepository;

@Service
public class PrivilegeService {

    @Autowired IPrivilegeRepository privilegeRepository;

    public PrivilegeService() {
        
    }

    public Privilege createPrivilegeIfNotFound(String name) {
 
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }
}
