package com.unimag.web.mapper;

import com.unimag.web.domain.Airport;
import com.unimag.web.api.dto.AirportDto;
import org.springframework.stereotype.Component;

@Component
public class AirportMapper {

    public AirportDto.AirportResponse toResponse(Airport airport) {
        if (airport == null) {
            return null;
        }
        return new AirportDto.AirportResponse(
                airport.getId(),
                airport.getCode(),
                airport.getName(),
                airport.getCity()
        );
    }

    public Airport toEntity(AirportDto.AirportCreateRequest request) {
        if (request == null) {
            return null;
        }
        Airport airport = new Airport();
        airport.setCode(request.code());
        airport.setName(request.name());
        airport.setCity(request.city());
        return airport;
    }
}