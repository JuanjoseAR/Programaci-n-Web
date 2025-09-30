package com.unimag.web.mapper;

import com.unimag.web.api.dto.BookingDto;
import com.unimag.web.domain.*;
import com.unimag.web.services.mapper.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    void setUp() {

        PassengerMapper passengerMapper = new PassengerMapper();
        AirlineMapper airlineMapper = new AirlineMapper();
        AirportMapper airportMapper = new AirportMapper();
        TagMapper tagMapper = new TagMapper();
        FlightMapper flightMapper = new FlightMapper(airlineMapper, airportMapper, tagMapper);
        bookingMapper = new BookingMapper(passengerMapper, flightMapper);
    }

    @Test
    void shouldMapBookingEntityToBookingResponseDto() {

        Passenger passenger = new Passenger();
        passenger.setId(1L);
        passenger.setFullName("Juan Perez");
        passenger.setEmail("juan@test.com");

        Flight flight = new Flight();
        flight.setId(100L);
        flight.setFlightNumber("AV24");

        BookingItem item = new BookingItem();
        item.setId(10L);
        item.setCabin(Cabin.ECONOMY);
        item.setPrice(new BigDecimal("250.75"));
        item.setFlight(flight);

        Booking bookingEntity = new Booking();
        bookingEntity.setId(5L);
        bookingEntity.setCreatedAt(OffsetDateTime.now());
        bookingEntity.setPassenger(passenger);
        bookingEntity.setItems(List.of(item));


        BookingDto.BookingResponse responseDto = bookingMapper.toResponse(bookingEntity);


        assertThat(responseDto).isNotNull();
        assertThat(responseDto.id()).isEqualTo(5L);
        assertThat(responseDto.passenger().email()).isEqualTo("juan@test.com");
        assertThat(responseDto.items()).hasSize(1);
        assertThat(responseDto.items().get(0).flight().number()).isEqualTo("AV24");
        assertThat(responseDto.items().get(0).price()).isEqualTo("250.75");
    }
}