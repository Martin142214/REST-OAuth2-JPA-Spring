package com.example.RestOAuth2JPA.DTO.entities.patient;

import java.util.Date;

import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.enums.PlaceOfTreatment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;

@Entity
@Table(name = "note_data")
public class NoteData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*@OneToOne(cascade = CascadeType.ALL, mappedBy = "data")
    private Note note;*/

    @Column(name = "patient_diagnosis")
    private String diagnosis;

    @Enumerated(EnumType.STRING)
    private PlaceOfTreatment placeOfTreatment;

    @Column(name = "description")
    private String description;

    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date dateOfNote;

    public Long getId() {
        return id;
    }

    public Date getDateOfNote() {
        return dateOfNote;
    }

    public void setDateOfNote(Date dateOfNote) {
        this.dateOfNote = dateOfNote;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        if (diagnosis == null) {
            this.diagnosis = "not selected";
        }
        this.diagnosis = diagnosis;
    }

    public PlaceOfTreatment getPlaceOfTreatment() {
        return placeOfTreatment;
    }

    public void setPlaceOfTreatment(PlaceOfTreatment placeOfTreatment) {
        this.placeOfTreatment = placeOfTreatment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!description.isBlank() || description != null) {
            this.description = description;
        }
    }

    public NoteData() {
        
    }

    public NoteData(String diagnosis, PlaceOfTreatment placeOfTreatment, String description, Date date) {
        setDiagnosis(diagnosis);
        setPlaceOfTreatment(placeOfTreatment);
        setDescription(description);
        setDateOfNote(date);
    }
}
