package com.unimag.web.services.impl;

import com.unimag.web.api.dto.PassengerDto.*;
import com.unimag.web.domain.Passenger;
import com.unimag.web.domain.PassengerProfile;
import com.unimag.web.repositories.PassengerProfileRepository;
import com.unimag.web.repositories.PassengerRepository;
import com.unimag.web.services.PassengerService;
import com.unimag.web.services.mapper.PassengerMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerServicesImpl implements PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerProfileRepository profileRepository;
    private final PassengerMapper passengerMapper;

    @Override
    @Transactional
    public PassengerResponse createPassenger(PassengerCreateRequest request) {
        Passenger passenger = new Passenger();
        passenger.setFullName(request.fullname());
        passenger.setEmail(request.email());

        if (request.profile() != null) {
            PassengerProfile profile = PassengerProfile.builder()
                    .phone(request.profile().phone())
                    .countryCode(request.profile().countryCode())
                    .build();
            profileRepository.save(profile);
            passenger.setProfile(profile);
        }

        passenger = passengerRepository.save(passenger);
        return passengerMapper.toResponse(passenger);
    }

    @Override
    @Transactional
    public PassengerResponse updatePassenger(PassengerUpdateRequest request) {
        Passenger passenger = passengerRepository.findByEmailIgnoreCase(request.email())
                .orElseThrow(() -> new EntityNotFoundException("Passenger not found with email: " + request.email()));

        passenger.setFullName(request.fullname());

        if (request.profile() != null) {
            PassengerProfile profile = passenger.getProfile();
            if (profile == null) {
                profile = new PassengerProfile();
            }
            profile.setPhone(request.profile().phone());
            profile.setCountryCode(request.profile().countryCode());
            profileRepository.save(profile);
            passenger.setProfile(profile);
        }

        passenger = passengerRepository.save(passenger);
        return passengerMapper.toResponse(passenger);
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse getByEmail(String email) {
        Passenger passenger = passengerRepository.fechtWhitProfileByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Passenger not found with email: " + email));
        return passengerMapper.toResponse(passenger);
    }

    @Override
    @Transactional(readOnly = true)
    public PassengerResponse getById(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Passenger not found with id: " + id));
        return passengerMapper.toResponse(passenger);
    }

    @Override
    @Transactional
    public void deletePassenger(Long id) {
        Passenger passenger = passengerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Passenger not found with id: " + id));
        passengerRepository.delete(passenger);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PassengerResponse> list(Pageable pageable) {
        return passengerRepository.findAll(pageable)
                .map(passengerMapper::toResponse);
    }
}
