package com.unimag.web.services;

import com.unimag.web.api.dto.PassengerDto.*;
import com.unimag.web.domain.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PassengerService {
    PassengerResponse createPassenger(PassengerCreateRequest passenger);
    PassengerResponse updatePassenger(PassengerUpdateRequest passenger);
    PassengerResponse getByEmail(String email);
    PassengerResponse getById(Long id);
    void deletePassenger(Long id);
    Page<PassengerResponse> list(Pageable pageable);
}
