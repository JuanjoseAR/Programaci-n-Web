package com.unimag.web.services.mapper;

import com.unimag.web.domain.Airline;
import com.unimag.web.api.dto.AirlineDto;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AirlineMapper {

    public AirlineDto.AirlineResponse toResponse(Airline airline) {
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

    public Airline toEntity(AirlineDto.AirlineCreateRequest request) {
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