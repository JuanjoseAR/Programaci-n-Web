package com.unimag.web.services.mapper;

import com.unimag.web.domain.Airline;
import com.unimag.web.domain.Airport;
import com.unimag.web.domain.Flight;
import com.unimag.web.domain.Tag;
import com.unimag.web.api.dto.AirlineDto;
import com.unimag.web.api.dto.AirportDto;
import com.unimag.web.api.dto.FlightDto;
import com.unimag.web.api.dto.TagDto;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FlightMapper {

    public static FlightDto.FlightResponse toResponse(Flight flight) {
        if (flight == null) {
            return null;
        }

        AirlineDto.AirlineResponse airlineResponse = AirlineMapper.toResponse(flight.getAirline());
        AirportDto.AirportResponse originResponse = AirportMapper.toResponse(flight.getOrigin());
        AirportDto.AirportResponse destinationResponse = AirportMapper.toResponse(flight.getDestination());

        Set<TagDto.TagResponse> tagsResponse = (flight.getTags() != null)
                ? flight.getTags().stream().map(TagMapper::toResponse).collect(Collectors.toSet())
                : Collections.emptySet();


        return new FlightDto.FlightResponse(
                flight.getId(),
                flight.getFlightNumber(),
                flight.getDepartureTime(),
                flight.getArrivalTime(),
                airlineResponse,
                originResponse,
                destinationResponse,
                tagsResponse
        );
    }

    public static Flight toEntity(FlightDto.FlightCreateRequest request) {
        if (request == null) {
            return null;
        }

        Flight flight = new Flight();
        flight.setFlightNumber(request.number());
        flight.setDepartureTime(request.departureTime());
        flight.setArrivalTime(request.arrivalTime());

        Airline airline = new Airline();
        airline.setId(request.airlineId());
        flight.setAirline(airline);

        Airport origin = new Airport();
        origin.setId(request.originAirportId());
        flight.setOrigin(origin);

        Airport destination = new Airport();
        destination.setId(request.destinationAirportId());
        flight.setDestination(destination);

        if (request.tagIds() != null) {
            Set<Tag> tags = request.tagIds().stream().map(tagId -> {
                Tag tag = new Tag();
                tag.setId(tagId);
                return tag;
            }).collect(Collectors.toSet());
            flight.setTags(tags);
        } else {
            flight.setTags(new HashSet<>());
        }

        return flight;
    }
}