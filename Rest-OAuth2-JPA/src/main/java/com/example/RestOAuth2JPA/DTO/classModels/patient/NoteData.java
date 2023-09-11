package com.example.RestOAuth2JPA.DTO.classModels.patient;

import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.enums.PlaceOfTreatment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class NoteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    private Note note;

    private String diagnosis;

    private PlaceOfTreatment placeOfTreatment;

    private String description;
}
