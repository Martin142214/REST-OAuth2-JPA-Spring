package com.example.RestOAuth2JPA.services.additional;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.HealRequest;
import com.example.RestOAuth2JPA.DTO.entities.Note;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.entities.patient.NoteData;
import com.example.RestOAuth2JPA.DTO.repositories.IDoctorRepository;
import com.example.RestOAuth2JPA.DTO.repositories.INotesRepository;
import com.example.RestOAuth2JPA.DTO.repositories.IPatientRepository;
import com.example.RestOAuth2JPA.DTO.repositories.auth.IUsersRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IHealRequestRepository;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.INoteDataRepository;
import com.example.RestOAuth2JPA.services.notes.NotesService;

@Service
public class DataAccessUtil {

    // User entities section
    @Autowired IUsersRepository usersRepository;

    // Note entities section
    @Autowired INotesRepository notesRepository;

    @Autowired IHealRequestRepository healRequestRepository;

    // Storage fields
    private List<User> allCurrentPatientUsersInDB;

    private List<User> allCurrentDoctorUsersInDB;

    private List<Note> allCurrentUserNotesInDB;

    private List<HealRequest> allCurrentHealRequestsInDB;

    @Autowired
    public DataAccessUtil(IUsersRepository usersRepository, INotesRepository notesRepository, IHealRequestRepository healRequestRepository) {
        this.usersRepository = usersRepository;
        this.notesRepository = notesRepository;
        this.healRequestRepository = healRequestRepository;
        executeFetch();
    }

    public List<HealRequest> getHealRequestsInDB() {
        return allCurrentHealRequestsInDB;
    }

    public List<User> getAllPatientUsers() {
        return allCurrentPatientUsersInDB;
    }

    public List<User> getAllDoctorUsers() {
        return allCurrentDoctorUsersInDB;
    }

    public List<Note> getAllUserNotes() {
        return allCurrentUserNotesInDB;
    }

    public void fetchDoctorUsers() {
        this.allCurrentDoctorUsersInDB = usersRepository.findAll().stream().filter(user -> user.getDoctor() != null).toList();
    }

    public void fetchPatientUsers() {
        this.allCurrentPatientUsersInDB = usersRepository.findAll().stream().filter(user -> user.getPatient() != null).toList();
    }

    public void fetchUserNotes() {
        this.allCurrentUserNotesInDB = notesRepository.findAll();
    }

    public void fetchHealRequests() {
        this.allCurrentHealRequestsInDB = healRequestRepository.findAll();
    }

    public void executeFetch() {
        fetchDoctorUsers();
        fetchPatientUsers();
        fetchUserNotes();
        fetchHealRequests();
    }

}

