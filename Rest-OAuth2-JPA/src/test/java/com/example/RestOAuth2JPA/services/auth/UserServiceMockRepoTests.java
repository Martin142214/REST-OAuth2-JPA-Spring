package com.example.RestOAuth2JPA.services.auth;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.enums.Roles;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceMockRepoTests {

    @Autowired
    private UserService userService;

    @MockBean
    private IUsersRepository mockUsersRepository;
    
    @Spy
    private List<User> allMockUsers = new ArrayList<>();
    
    @BeforeAll
    public void setUp() {
        Optional<User> mockDoctorUser = Optional.of(new User(Long.valueOf(3), "doctor1", "doctor1@abv.bg", new Role(Roles.ROLE_DOCTOR.toString()), new Doctor(), null));
        Optional<User> mockPatientUser = Optional.of(new User(Long.valueOf(2), "user1", "user1@abv.bg", new Role(Roles.ROLE_PATIENT.toString()), null, new Patient()));
        Optional<User> mockAdminUser = Optional.of(new User(Long.valueOf(1), "admin123", "admin123@abv.bg", new Role(Roles.ROLE_ADMIN.toString()), null, null));
    
        //Mock users for findById()      
        when(mockUsersRepository.findById(Long.valueOf(3))).thenReturn(mockDoctorUser);
        when(mockUsersRepository.findById(Long.valueOf(2))).thenReturn(mockPatientUser);
        when(mockUsersRepository.findById(Long.valueOf(1))).thenReturn(mockAdminUser);

        //Mock users for findByUsername()      
        when(mockUsersRepository.findByUsername(mockDoctorUser.get().getUsername())).thenReturn(mockDoctorUser.get());
        when(mockUsersRepository.findByUsername(mockPatientUser.get().getUsername())).thenReturn(mockPatientUser.get());

        //allMockUsers.add(mockAdminUser.get());
        //allMockUsers.add(mockPatientUser.get());
        //allMockUsers.add(mockDoctorUser.get());

    }

    @AfterEach
    public void endUp() {
        //allMockUsers.clear();
    }


    @Test
    public void whenSaveNewEntityUsingUsersRepository_thenNewUserEntityShouldBeSaved() throws Exception {

        when(mockUsersRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User user = new User("username", "user@abv.bg");
        userService.saveNewOrUpdateUser(user);

        verify(mockUsersRepository, times(1)).save(any(User.class));
    }

    @Test
    public void whenDeleteUserEntityUsingUsersRepository_thenExistingUserEntityShouldBeDeleted() throws Exception {
        when(mockUsersRepository.findById(any(Long.class))).thenReturn(Optional.of(new User()));
    
        userService.deleteUser(Long.valueOf(4));

        verify(mockUsersRepository, times(1)).delete(any(User.class));
    }

}
