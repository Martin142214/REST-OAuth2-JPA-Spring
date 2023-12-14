package com.example.RestOAuth2JPA.DTO.entities.auth;

import com.example.RestOAuth2JPA.DTO.entities.FileDB;
import com.example.RestOAuth2JPA.DTO.entities.doctor.Doctor;
import com.example.RestOAuth2JPA.DTO.entities.patient.Patient;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 24)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_email", unique = true, nullable = false)
    private String email;

    @Column(name = "isEnabled", nullable = false)
    private boolean enabled;

    @Column(name = "isExpired", nullable = false)
    private boolean tokenExpired;

    @Column(name = "isAdmin", nullable = false)
    private boolean isAdmin;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;

    //TEST constructor
    //TODO Remove later
    public User(Long id, String username, String email, Role role, Doctor doctor, Patient patient) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.doctor = doctor;
        this.patient = patient;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private FileDB profileImage;

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    
    public User(String username, String password, String email, 
                Role role, Patient patient, boolean isEnabled, 
                boolean isTokenExpired, boolean isAdmin, FileDB profileImage) {

        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.enabled = isEnabled;
        this.tokenExpired = isTokenExpired;
        this.isAdmin = isAdmin;
        this.profileImage = profileImage;
    }

    public User() {
        
    }

    

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    // ctor for test purposes
    public User(Long id, String username) {
        setId(id);
        setUsername(username);
    }
    
    /*@ManyToMany 
    @JoinTable( 
        name = "users_roles", 
        joinColumns = @JoinColumn(
          name = "user_id", referencedColumnName = "id"), 
        inverseJoinColumns = @JoinColumn(
          name = "role_id", referencedColumnName = "id")) 
    private Collection<Role> roles;*/


    /*@OneToOne(fetch = FetchType.LAZY, mappedBy = "user")
    private Patient patient;*/

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public FileDB getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(FileDB profileImage) {
        this.profileImage = profileImage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
