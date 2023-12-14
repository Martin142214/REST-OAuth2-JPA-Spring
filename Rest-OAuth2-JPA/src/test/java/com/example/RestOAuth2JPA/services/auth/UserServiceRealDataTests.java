package com.example.RestOAuth2JPA.services.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.RestOAuth2JPA.DTO.entities.auth.Role;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.enums.Roles;
import com.example.RestOAuth2JPA.services.userDetails.CustomUserDetails;
import com.example.RestOAuth2JPA.services.userDetails.UserDetailsServiceImplementation;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class UserServiceRealDataTests {
    
    @Autowired
    private UserService userService;

    //@MockBean
    @Autowired
    private IUsersRepository usersRepository;

    @Spy
    private UserDetailsServiceImplementation userDetailsService;


    @BeforeAll
    public void whenSetMockDoctorUserCredentials_thenDoctorUserShouldBeAuthenticatedAndAuthorized() {
        CustomUserDetails userDetails = new CustomUserDetails("doctor1", usersRepository);
    
        var principal = userDetails.getUsername();
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", principal);
        credentials.put("password", "doctor1");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(principal, credentials, userDetails.getAuthorities());

        //Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
        
        Mockito.when(SecurityContextHolder.getContext().getAuthentication()).thenReturn(authenticationToken);
    }


    @Test
    public void whenProvideNullArgument_thenNoUserShouldBeSaved() {
        User mockUser = null;

        userService.setCurrentUser(mockUser);

        assertEquals(userService.getCurrentUser(), null);
    }

    @Test
    public void whenSetUser_thenCurrentUserIsNotNull() {
        User mockUser = new User();

        userService.setCurrentUser(mockUser);

        assertNotEquals(userService.getCurrentUser(), null);
    }

    @Test
    public void whenSetUser_thenUserMethodSetsTheUserCorrectly() {
        //Arrange
        User mockUser = new User(Long.valueOf(10), "mockUser");

        //Act
        userService.setCurrentUser(mockUser);

        //Assert
        assertEquals(mockUser.getUsername(), "mockUser");
        assertEquals(mockUser.getId(), Long.valueOf(10));
    }


    @Test
    public void checkUserIsFetchedCorrectlyWithMockSecurityContext() {

        //Arrange
        User expectedDoctorUser = usersRepository.findByUsername("doctor1");

        //Act
        User actualDoctorUser = userService.getCurrentlyLoggedInUser();

        boolean authenticated = userService.authenticated();

        //Assert
        assertTrue(authenticated);
        assertEquals(expectedDoctorUser.getId(), actualDoctorUser.getId());
        
    }

    @Test
    public void whenThereIsOnlyOneDoctorUser_checkTheExistingDoctorUserHasTheExpectedData() {

        //Arrange
        List<User> expectedDoctorUsers = new ArrayList<>();
        expectedDoctorUsers.add(new User(Long.valueOf(3), "doctor1", "doctor1@abv.bg", new Role(Roles.ROLE_DOCTOR.toString()), new Doctor(), null));
        
        List<User> doctorUsers = userService.getAllDoctorUsers();

        //Act
        User expectedUser = expectedDoctorUsers.get(0);
        User actualUser = doctorUsers.get(0);
        
        //Assert
        //TODO The size will not be the same in other cases
        assertEquals(expectedDoctorUsers.size(), doctorUsers.size());
        assertEquals(expectedUser.getId(), actualUser.getId());
    }

    @Test
	public void checkDoctorHasTheDoctorRoleAuthority() {
		
		//Arrange
		SimpleGrantedAuthority expectedAuthorityForDoctor = new SimpleGrantedAuthority("ROLE_DOCTOR");

		UserDetails doctorUser = userDetailsService.loadUserByUsername("doctor1", usersRepository);
		Collection<? extends GrantedAuthority> collection = doctorUser.getAuthorities();
		
		//Act
		Optional<? extends GrantedAuthority> authority = collection.stream().filter(a -> a.getAuthority().equals("ROLE_DOCTOR")).findAny();
		SimpleGrantedAuthority actualResult = null;
		if (authority.isPresent()) {
			actualResult = new SimpleGrantedAuthority(authority.get().getAuthority());		
		}

		//Assert
		assertNotNull(actualResult);
		assertEquals(expectedAuthorityForDoctor, actualResult);
	}

    @Test
	//TEST fails because userToExpect has no role info fetched
	//TODO
	public void checkDoctorUserIsFetchedCorrectlyOnLogin() {

		//Arrange
		User userToExpect = usersRepository.findByUsername("doctor1");
		
		//Act
		UserDetails details = userDetailsService.loadUserByUsername("doctor1", usersRepository);
		
		//Assert
		assertEquals(userToExpect.getPassword(), details.getPassword());	
	}
}
