package com.example.RestOAuth2JPA.services.requests;

import com.example.RestOAuth2JPA.enums.HealRequestStatus;

public interface IHealRequestService {

    boolean alreadyHasSuchHealRequest(final Long doctorId, final Long patientId);

    void setHealRequestStatusAndSaveUsersInDB(final Long patientId, final Long requestId, final HealRequestStatus status);

}
