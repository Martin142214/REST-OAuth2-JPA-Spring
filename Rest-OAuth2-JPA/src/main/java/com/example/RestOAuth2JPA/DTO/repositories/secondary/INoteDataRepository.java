package com.example.RestOAuth2JPA.DTO.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.RestOAuth2JPA.DTO.classModels.patient.NoteData;

public interface INoteDataRepository extends JpaRepository<NoteData, Long> {
    
}
