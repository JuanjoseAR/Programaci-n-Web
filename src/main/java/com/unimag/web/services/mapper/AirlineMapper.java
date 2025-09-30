package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.AirlineDto;
import com.unimag.web.domain.Airline;

import java.util.Collections;


public class AirlineMapper {

    public static AirlineDto.AirlineResponse toResponse(Airline airline) {
        if (airline == null) {
            return null;
        }

        return new AirlineDto.AirlineResponse(
                airline.getId(),
                airline.getCode(),
                airline.getName(),
                Collections.emptyList()
        );
    }

    public static Airline toEntity(AirlineDto.AirlineCreateRequest request) {
        if (request == null) {
            return null;
        }

        Airline airline = new Airline();
        airline.setCode(request.code());
        airline.setName(request.name());
        airline.setFlights(Collections.emptyList());

        return airline;
    }
}