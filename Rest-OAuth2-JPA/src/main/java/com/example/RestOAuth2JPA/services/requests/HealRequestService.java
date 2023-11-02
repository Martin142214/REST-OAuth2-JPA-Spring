package com.example.RestOAuth2JPA.services.requests;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.HealRequest;
import com.example.RestOAuth2JPA.DTO.entities.auth.User;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IHealRequestRepository;
import com.example.RestOAuth2JPA.enums.HealRequestStatus;
import com.example.RestOAuth2JPA.services.auth.UserService;

@Service
public class HealRequestService implements IHealRequestService {

    @Autowired IHealRequestRepository healRequestRepository;

    @Autowired UserService userService;

    private Collection<HealRequest> allUpToDateHealRequestsInDB;

    @Autowired
    public HealRequestService(IHealRequestRepository healRequestRepository) {
        this.healRequestRepository = healRequestRepository;
        this.allUpToDateHealRequestsInDB = this.healRequestRepository.findAll();
    }

    public Optional<HealRequest> findHealRequestById(final Long id) {
        return healRequestRepository.findById(id);
    }

    public boolean alreadyHasSuchHealRequest(final Long doctorId, final Long patientId) {
        
        Optional<HealRequest> request = allUpToDateHealRequestsInDB.stream()
                                                                    .filter(req -> req.getDoctor().getId().equals(doctorId) && 
                                                                                      req.getPatient().getId().equals(patientId))
                                                                    .findAny();
        return request.isPresent();
    }

    public void setHealRequestStatusAndSaveUsersInDB(final Long patientId, final Long requestId, final HealRequestStatus status) {
        User doctorUser = userService.getCurrentlyLoggedInUser();
        Optional<User> patientUser = userService.findById(patientId);
        Optional<HealRequest> request = this.findHealRequestById(requestId);
        if (request.isPresent()) {
            if (status.equals(HealRequestStatus.Approved) && patientUser.isPresent()) {
                doctorUser.getDoctor().addPatient(patientUser.get().getPatient());
                patientUser.get().getPatient().setDoctor(doctorUser.getDoctor());
            }
            request.get().setRequestStatus(status);

            try {
                healRequestRepository.save(request.get());
                userService.saveNewOrUpdateUser(patientUser.get());
                userService.saveNewOrUpdateUser(doctorUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                this.allUpToDateHealRequestsInDB = healRequestRepository.findAll();
            }
        }
    }


}
