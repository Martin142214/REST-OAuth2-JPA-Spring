package com.example.RestOAuth2JPA.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.example.RestOAuth2JPA.enums.PlaceOfTreatment;
import com.example.RestOAuth2JPA.services.additional.RedirectHandler;
import com.example.RestOAuth2JPA.services.auth.UserService;
import com.example.RestOAuth2JPA.services.notes.NotesService;

@Controller
@RequestMapping("/user/patient")
public class NotesController {

    @Autowired UserService userService;

    @Autowired NotesService notesService;

    @Autowired RedirectHandler redirectHandler;

    public NotesController() {
        
    }

    @PostMapping("/{id}/notes/add")
    @Secured("ROLE_DOCTOR")
    public RedirectView add_note_to_patient(@PathVariable Long id, Long doctorId, String diagnosis, String description, PlaceOfTreatment placeOfTreatment) {

        notesService.createNoteForPatient(id, doctorId, diagnosis, placeOfTreatment, description);
        return redirectHandler.redirectView(redirectHandler.patientProfileUrl + "/" + id);
    }

    @PostMapping("/{id}/notes/{noteId}/delete")
    @Secured("ROLE_DOCTOR")
    public RedirectView delete_note_for_patient(@PathVariable Long id, @PathVariable Long noteId) {

        notesService.delete(noteId);
        return redirectHandler.redirectView(redirectHandler.patientProfileUrl + "/" + id);
    }
}
