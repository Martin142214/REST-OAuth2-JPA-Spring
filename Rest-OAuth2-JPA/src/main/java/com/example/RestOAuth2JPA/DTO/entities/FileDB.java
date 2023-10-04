package com.example.RestOAuth2JPA.DTO.entities;

import com.example.RestOAuth2JPA.DTO.entities.auth.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_images")
public class FileDB {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @Column(name = "imageName", nullable = false)
    public String name;

    @Column(name = "imageExtension", nullable = false)
    public String type;

    @Column(name = "imagePath", nullable = false)
    public String imagePath; 

    //@OneToOne(fetch = FetchType.LAZY, mappedBy = "profileImage")
    //private User user;

    public FileDB() {
    }

    public FileDB(String name, String type, String imagePath) {
        this.name = name;
        this.type = type;
        this.imagePath = imagePath;
    }
}
