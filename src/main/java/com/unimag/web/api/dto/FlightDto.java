package com.unimag.web.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public class FlightDto {

    public record FlightCreateRequest(
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            Long airlineId,
            Long originAirportId,
            Long destinationAirportId,
            Set<Long> tagIds
    ) implements Serializable {}

    public record FlightUpdateRequest(
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            Long airlineId,
            Long originAirportId,
            Long destinationAirportId,
            Set<Long> tagIds
    ) implements Serializable {}

    public record FlightResponse(
            Long id,
            String number,
            OffsetDateTime departureTime,
            OffsetDateTime arrivalTime,
            AirlineDto.AirlineResponse airline,
            AirportDto.AirportResponse origin,
            AirportDto.AirportResponse destination,
            Set<TagDto.TagResponse> tags
    ) implements Serializable {}
}
