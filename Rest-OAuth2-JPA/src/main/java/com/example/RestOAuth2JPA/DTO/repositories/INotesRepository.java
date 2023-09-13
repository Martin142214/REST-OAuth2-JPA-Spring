package com.example.RestOAuth2JPA.DTO.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.entities.Note;

public interface INotesRepository extends JpaRepository<Note, Long> {
    
}
