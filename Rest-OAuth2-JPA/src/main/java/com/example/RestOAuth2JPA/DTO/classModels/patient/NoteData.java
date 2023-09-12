package com.example.RestOAuth2JPA.DTO.classModels.patient;

import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.enums.PlaceOfTreatment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "note_data")
public class NoteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "note_id", referencedColumnName = "id")
    private Note note;

    @Transient
    private String diagnosis;

    @Enumerated(EnumType.STRING)
    private PlaceOfTreatment placeOfTreatment;

    @Transient
    private String description;
}
