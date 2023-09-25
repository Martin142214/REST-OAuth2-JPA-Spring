package com.example.RestOAuth2JPA.DTO.repositories.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;

public interface IUsersRepository extends JpaRepository<User, Long> {
    
    //@Query("SELECT u FROM User WHERE u.username = :username")
    public User findByUsername(String username);
    
    public User findByEmail(String email);

    //@Query("SELECT case when count(c)> 0 then true else false end FROM User u WHERE lower(u.username) like lower(:username)")
    //@Query(value = "select if(count(name) > 0, 'true', 'false') from User user where user.name = :username", nativeQuery = true)
    //public Boolean userExistsByUsername(String username);
}
