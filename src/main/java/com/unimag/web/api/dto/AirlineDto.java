package com.unimag.web.api.dto;

import java.io.Serializable;
import java.util.List;

public class AirlineDto {

    public record AirlineCreateRequest(
            String code,
            String name
    ) implements Serializable {}

    public record AirlineUpdateRequest(
            String code,
            String name
    ) implements Serializable {}

    public record AirlineResponse(
            Long id,
            String code,
            String name,
            List<FlightDto.FlightResponse> flights
    ) implements Serializable {}
}
