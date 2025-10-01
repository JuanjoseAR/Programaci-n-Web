package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.AirportDto.*;
import com.unimag.web.domain.Airport;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AirportMapper {


    AirportResponse toResponse(Airport airport);


    Airport toEntity(AirportCreateRequest request);

    Airport toEntity(AirportUpdateRequest request);
}
