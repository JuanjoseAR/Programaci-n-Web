package com.unimag.web.services.mapper;

import com.unimag.web.domain.Passenger;
import com.unimag.web.api.dto.PassengerDto;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {

    public PassengerDto.PassengerResponse toResponse(Passenger passenger) {
        if (passenger == null) return null;

        PassengerDto.PassengerProfileDto profileDto = null;
        if (passenger.getProfile() != null) {
            profileDto = new PassengerDto.PassengerProfileDto(
                    passenger.getProfile().getPhone(),
                    passenger.getProfile().getCountryCode()
            );
        }

        return new PassengerDto.PassengerResponse(
                passenger.getId(),
                passenger.getFullName(),
                passenger.getEmail(),
                profileDto
        );
    }

    public Passenger toEntity(PassengerDto.PassengerCreateRequest request) {
        if (request == null) {
            return null;
        }

        Passenger passenger = new Passenger();
        passenger.setFullName(request.fullname());
        passenger.setEmail(request.email());

        return passenger;
    }
}