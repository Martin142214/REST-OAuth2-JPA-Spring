package com.example.RestOAuth2JPA.services.requests;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.example.RestOAuth2JPA.DTO.entities.HealRequest;
import com.example.RestOAuth2JPA.DTO.repositories.secondary.IHealRequestRepository;

@Service
public class HealRequestService {

    @Autowired IHealRequestRepository healRequestRepository;

    public HealRequestService() {
        
    }

    public boolean alreadyHasSuchHealRequest(Long doctorId, Long patientId) {
        
        Optional<HealRequest> request = healRequestRepository.findAll().stream()
                                                                       .filter(req -> req.getDoctor().getId().equals(doctorId) && 
                                                                                      req.getPatient().getId().equals(patientId))
                                                                       .findAny();
        return request.isPresent();
    }


}
