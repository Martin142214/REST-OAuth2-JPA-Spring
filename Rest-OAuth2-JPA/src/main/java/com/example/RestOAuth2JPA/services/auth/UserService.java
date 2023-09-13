package com.example.RestOAuth2JPA.services.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;

@Service
public class UserService {

    @Autowired IUsersRepository _usersRepository;

    public UserService() {
        
    }

    public void save_new_user(User user) {
        _usersRepository.save(user);
    }

    public boolean emailExist(String email){
        User user = _usersRepository.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    public boolean usernameExist(String username){
        User user = _usersRepository.findByUsername(username);
        if (user != null) {
            return true;
        }
        return false;
    }
}
