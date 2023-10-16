package com.example.RestOAuth2JPA.controllers;

import java.lang.ProcessBuilder.Redirect;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.NoteData;
import com.example.RestOAuth2JPA.DTO.repositories.INotesRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IHealRequestRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.INoteDataRepository;
import com.example.RestOAuth2JPA.enums.PlaceOfTreatment;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.requests.HealRequestService;

@Controller
public class NotesController {

    @Autowired UserService userService;

    @Autowired INotesRepository notesRepository;

    @Autowired INoteDataRepository noteDataRepository;

    @Autowired RedirectHandler redirectHandler;

    public NotesController() {
        
    }

    @PostMapping("/user/patient/{id}/notes/add")
    @Secured("ROLE_DOCTOR")
    public RedirectView add_note_to_patient(@PathVariable Long id, Long doctorId, String diagnosis, String description, PlaceOfTreatment placeOfTreatment) {
        Optional<User> userPatient = userService.findById(id);
        if (userPatient.isPresent() && doctorId != null) {
            Note note = new Note();
            note.setPatient(userPatient.get().getPatient());
            note.setDoctor(userService.findById(doctorId).get().getDoctor());
            if (diagnosis != null && description != null && placeOfTreatment != null) {
                NoteData noteData = new NoteData(diagnosis, placeOfTreatment, description);
                note.setData(noteData);

                noteDataRepository.save(noteData);
                notesRepository.save(note);
            }
        }
        return redirectHandler.redirectView(redirectHandler.patientProfileUrl + "/" + id);
    }
}
