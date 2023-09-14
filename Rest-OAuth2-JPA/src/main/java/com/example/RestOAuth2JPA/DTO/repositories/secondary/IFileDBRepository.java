package com.example.RestOAuth2JPA.DTO.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.FileDB;

public interface IFileDBRepository extends JpaRepository<FileDB, Long>{
     
}
