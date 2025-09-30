package com.unimag.web.services.mapper;

import com.unimag.web.domain.Passenger;
import com.unimag.web.api.dto.PassengerDto;
import org.springframework.stereotype.Component;

@Component
public class PassengerMapper {

    public static PassengerDto.PassengerResponse toResponse(Passenger passenger) {
        if (passenger == null) {
            return null;
        }

        return new PassengerDto.PassengerResponse(
                passenger.getEmail(),
                passenger
        );
    }

    public static Passenger toEntity(PassengerDto.PassengerCreateRequest request) {
        if (request == null) {
            return null;
        }

        Passenger passenger = new Passenger();
        passenger.setFullName(request.fullname());
        passenger.setEmail(request.email());

        return passenger;
    }
}