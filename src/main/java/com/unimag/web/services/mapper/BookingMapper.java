package com.unimag.web.services.mapper;

import com.unimag.web.api.dto.BookingDto.*;
import com.unimag.web.domain.Booking;
import com.unimag.web.domain.BookingItem;
import com.unimag.web.domain.Cabin;
import com.unimag.web.domain.Flight;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(
        componentModel = "spring",
        uses = { PassengerMapper.class, FlightMapper.class }
)
public interface BookingMapper {


    @Mapping(source = "passenger", target = "passenger")
    BookingResponse toResponse(Booking booking);

    @Mapping(source = "flight", target = "flight")
    @Mapping(source = "cabin", target = "cabin")
    @Mapping(source = "price", target = "price")
    BookingItemResponse toItemResponse(BookingItem item);


    @Mapping(source = "passengerId", target = "passenger.id")
    Booking toEntity(BookingCreateRequest request);

    @Mapping(source = "flightId", target = "flight.id")
    @Mapping(source = "cabin", target = "cabin")
    @Mapping(source = "price", target = "price")
    BookingItem toItemEntity(BookingItemCreateRequest item);


    default String map(BigDecimal value) {
        return value != null ? value.toString() : null;
    }

    default BigDecimal map(String value) {
        return value != null ? new BigDecimal(value) : null;
    }

    default String map(Cabin cabin) {
        return cabin != null ? cabin.name() : null;
    }

    default Cabin mapCabin(String cabin) {
        return cabin != null ? Cabin.valueOf(cabin) : null;
    }

    default Flight mapFlight(Long flightId) {
        if (flightId == null) return null;
        Flight flight = new Flight();
        flight.setId(flightId);
        return flight;
    }
}
