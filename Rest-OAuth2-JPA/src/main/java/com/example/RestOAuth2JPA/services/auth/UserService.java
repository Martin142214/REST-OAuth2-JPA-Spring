package com.example.RestOAuth2JPA.services.auth;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

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

    public List<User> get_all_users() {
        return _usersRepository.findAll();
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

    public RedirectView redirectView(String url){
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url);
        return redirectView;
    }
}
