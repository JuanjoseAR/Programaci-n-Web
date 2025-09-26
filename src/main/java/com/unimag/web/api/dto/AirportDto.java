package com.unimag.web.api.dto;

import java.io.Serializable;

public class AirportDto {

    public record AirportCreateRequest(
            String code,
            String name,
            String city
    ) implements Serializable {}

    public record AirportUpdateRequest(
            String code,
            String name,
            String city
    ) implements Serializable {}

    public record AirportResponse(
            Long id,
            String code,
            String name,
            String city
    ) implements Serializable {}
}
