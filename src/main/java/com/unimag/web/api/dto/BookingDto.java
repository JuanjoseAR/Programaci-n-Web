package com.unimag.web.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingDto {

    public record BookingCreateRequest(
            Long passengerId,
            List<BookingItemCreateRequest> items
    ) implements Serializable {}

    public record BookingResponse(
            Long id,
            OffsetDateTime createdAt,
            PassengerDto.PassengerResponse passenger,
            List<BookingItemResponse> items
    ) implements Serializable {}

    public record BookingItemCreateRequest(
            String cabin,
            String price,
            Integer segmentOrder,
            Long flightId
    ) implements Serializable {}

    public record BookingItemResponse(
            Long id,
            String cabin,
            String price,
            Integer segmentOrder,
            FlightDto.FlightResponse flight
    ) implements Serializable {}
}
