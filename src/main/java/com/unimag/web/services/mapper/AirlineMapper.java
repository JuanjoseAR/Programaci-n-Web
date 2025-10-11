package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.AirlineDto.*;
import com.unimag.web.domain.Airline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Collections;

@Mapper(componentModel = "spring", uses = { FlightMapper.class })
public interface AirlineMapper {


    @Mapping(source = "flights", target = "flights")
    AirlineResponse toResponse(Airline airline);

    @Mapping(target = "flights", ignore = true)
    Airline toEntity(AirlineCreateRequest request);

    @Mapping(target = "flights", ignore = true)
    Airline toEntity(AirlineUpdateRequest request);
}
