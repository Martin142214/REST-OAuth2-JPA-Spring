package com.example.RestOAuth2JPA.services.notes;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.NoteData;
import com.example.RestOAuth2JPA.DTO.repositories.INotesRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.INoteDataRepository;
import com.example.RestOAuth2JPA.enums.PlaceOfTreatment;
import com.example.RestOAuth2JPA.services.additional.DataAccessUtil;
import com.example.RestOAuth2JPA.services.auth.UserService;

@Service
public class NotesService {

    @Autowired INotesRepository notesRepository;

    @Autowired INoteDataRepository noteDataRepository;

    @Autowired DataAccessUtil dataAccessUtil;

    @Autowired UserService userService;
    
    public NotesService() {
        
    }

    public Note getNoteById(final Long id) {
        Optional<Note> note = notesRepository.findById(id);
        return note.orElseThrow();
    }

    public NoteData getNoteDataById(final Long id) {
        Optional<NoteData> noteData = noteDataRepository.findById(id);
        return noteData.orElseThrow();
    }

    // No update occurs on notes; they are only deleted by the doctor when expire
    /*public void update(final Note note, final NoteData noteData) {
        noteDataRepository.save(noteData);
        notesRepository.save(note); 
        
        dataAccessUtil.fetchUserNotes();
    }*/

    public void delete(final Long noteId) {
        Note note = this.getNoteById(noteId);
        NoteData dataForNote = this.getNoteDataById(note.getData().getId());

        noteDataRepository.delete(dataForNote);
        notesRepository.delete(note);

        dataAccessUtil.fetchUserNotes();
    }

    public void createNoteForPatient(final Long patientId, 
                       final Long doctorId, 
                       final String diagnosis, 
                       final PlaceOfTreatment placeOfTreatment, 
                       final String description) {

        User userPatient = userService.getAnyUserById(patientId);
        User userDoctor = userService.getAnyUserById(doctorId);

        NoteData noteData = new NoteData(diagnosis, placeOfTreatment, description, new Date());

        createNote(userDoctor, userPatient, noteData);
        // what if the userPatient is null
        // what if the user doctor is null
        // what if the description is null
        // check if dataAccessUtils.notes are equal to notes in db after I call the method
        // check if noteData is null
    }

    private void createNote(User doctor, User patient, NoteData noteData) {
        if (doctor != null || patient != null) {
            Note note = new Note();
            note.setData(noteData);
            note.setDoctor(doctor.getDoctor());
            note.setPatient(patient.getPatient());

            noteDataRepository.save(noteData);
            notesRepository.save(note);   
            
            dataAccessUtil.fetchUserNotes();
        }
    }




}
