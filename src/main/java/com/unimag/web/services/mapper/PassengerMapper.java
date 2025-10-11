package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.PassengerDto.*;
import com.unimag.web.domain.Passenger;
import com.unimag.web.domain.PassengerProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PassengerMapper {


    @Mapping(source = "fullName", target = "fullname")
    PassengerResponse toResponse(Passenger passenger);


    @Mapping(source = "fullname", target = "fullName")
    Passenger toEntity(PassengerCreateRequest request);


    @Mapping(source = "fullname", target = "fullName")
    Passenger toEntity(PassengerUpdateRequest request);


    PassengerProfileDto toDto(PassengerProfile profile);

    PassengerProfile toEntity(PassengerProfileDto profileDto);
}
