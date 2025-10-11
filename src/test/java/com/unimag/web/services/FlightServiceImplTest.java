package com.unimag.web.services;

import com.unimag.web.api.dto.FlightDto.*;
import com.unimag.web.domain.Airline;
import com.unimag.web.domain.Airport;
import com.unimag.web.domain.Flight;
import com.unimag.web.domain.Tag;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.*;
import com.unimag.web.services.impl.FlightServiceImpl;
import com.unimag.web.services.mapper.FlightMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.OffsetDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock FlightRepository flightRepo;
    @Mock AirlineRepository airlineRepo;
    @Mock AirportRepository airportRepo;
    @Mock TagRepository tagRepo;
    @Mock FlightMapper flightMapper;

    @InjectMocks
    FlightServiceImpl service;

    @Test
    void shouldCreateFlightAndReturnResponse() {
        var request = new FlightCreateRequest(
                "UN123",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                1L, 2L, 3L,
                Set.of()
        );

        var airline = new Airline(); airline.setId(1L);
        var origin  = new Airport(); origin.setId(2L);
        var dest    = new Airport(); dest.setId(3L);

        var entity = new Flight(); entity.setId(10L); entity.setNumber("UN123");
        var response = new FlightResponse(10L, "UN123",
                request.departureTime(), request.arrivalTime(),
                null, null, null, Set.of());

        when(airlineRepo.findById(1L)).thenReturn(Optional.of(airline));
        when(airportRepo.findById(2L)).thenReturn(Optional.of(origin));
        when(airportRepo.findById(3L)).thenReturn(Optional.of(dest));

        when(flightMapper.toEntity(request)).thenReturn(entity);
        when(flightRepo.save(any(Flight.class))).thenReturn(entity);
        when(flightMapper.toResponse(entity)).thenReturn(response);

        var result = service.create(request);

        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.number()).isEqualTo("UN123");
        verify(flightRepo).save(any(Flight.class));
    }

    @Test
    void shouldThrowWhenAirlineNotFound() {
        var request = new FlightCreateRequest(
                "X",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(1),
                9L, 2L, 3L,
                Set.of()
        );
        when(airlineRepo.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Airline 9 not found");
    }

    @Test
    void shouldGetFlightById() {
        var flight = new Flight(); flight.setId(5L);

        var response = new FlightResponse(
                5L, "AA101",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                null, null, null,
                Set.of()
        );

        when(flightRepo.findById(5L)).thenReturn(Optional.of(flight));
        when(flightMapper.toResponse(flight)).thenReturn(response);

        var result = service.get(5L);

        assertThat(result.id()).isEqualTo(5L);
        assertThat(result.number()).isEqualTo("AA101");
        verify(flightRepo).findById(5L);
    }

    @Test
    void shouldReturnFlightsByAirlineName() {
        var flight = new Flight(); flight.setId(1L);

        var response = new FlightResponse(
                1L, "XY1",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(1),
                null, null, null,
                Set.of()
        );

        var page = new PageImpl<>(List.of(flight));

        when(flightRepo.findByAirlineName(eq("LATAM"), any(Pageable.class))).thenReturn(page);
        when(flightMapper.toResponse(flight)).thenReturn(response);

        var result = service.listByAirline("LATAM", PageRequest.of(0, 10));

        assertThat(result).hasSize(1);
        assertThat(result.getContent().get(0).id()).isEqualTo(1L);
        assertThat(result.getContent().get(0).number()).isEqualTo("XY1");

        verify(flightRepo).findByAirlineName(eq("LATAM"), any(Pageable.class));
        verify(flightMapper).toResponse(flight);
    }

    @Test
    void shouldSearchFlightsBetweenDates() {
        var flight = new Flight(); flight.setId(7L);
        var response = new FlightResponse(
                7L, "COPA7",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                null, null, null, Set.of()
        );
        var page = new PageImpl<>(List.of(flight));

        when(flightRepo.search(
                isNull(),
                eq("BOG"),
                eq("MIA"),
                isNull(),
                any(OffsetDateTime.class),
                any(OffsetDateTime.class),
                any(Pageable.class)
        )).thenReturn(page);

        when(flightMapper.toResponse(flight)).thenReturn(response);

        var result = service.search(
                null, "BOG", "MIA", null,
                OffsetDateTime.now(),
                OffsetDateTime.now().plusDays(1),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent())
                .extracting(FlightResponse::id)
                .containsExactly(7L);
    }

    @Test
    void shouldSearchWithTags() {
        var flight = new Flight(); flight.setId(12L);
        var response = new FlightResponse(
                12L, "TAG12",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(2),
                null, null, null, Set.of()
        );

        when(flightRepo.findByAllTags(List.of("Promo"), 1))
                .thenReturn(List.of(flight));
        when(flightMapper.toResponse(flight)).thenReturn(response);

        var result = service.searchWithTags(List.of("Promo"));

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(12L);
    }

    @Test
    void shouldReturnEmptyWhenTagsEmpty() {
        var result = service.searchWithTags(Collections.emptyList());
        assertThat(result).isEmpty();
    }

    @Test
    void shouldAddTagToFlight() {
        var flight = new Flight(); flight.setId(20L); flight.setTags(new HashSet<>());
        var tag = new Tag(); tag.setId(30L);

        var response = new FlightResponse(
                20L, "ADD",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(1),
                null, null, null, Set.of()
        );

        when(flightRepo.findById(20L)).thenReturn(Optional.of(flight));
        when(tagRepo.findById(30L)).thenReturn(Optional.of(tag));
        when(flightMapper.toResponse(flight)).thenReturn(response);

        var result = service.addTag(20L, 30L);

        assertThat(flight.getTags()).contains(tag);
        assertThat(result.id()).isEqualTo(20L);
        verify(flightRepo).findById(20L);
        verify(tagRepo).findById(30L);
    }

    @Test
    void shouldRemoveTagFromFlight() {
        var tag = new Tag(); tag.setId(40L); tag.setFlights(new HashSet<>());
        var flight = new Flight(); flight.setId(21L); flight.setTags(new HashSet<>(Set.of(tag)));
        tag.getFlights().add(flight);

        var response = new FlightResponse(
                21L, "REM",
                OffsetDateTime.now(),
                OffsetDateTime.now().plusHours(1),
                null, null, null, Set.of()
        );

        when(flightRepo.findById(21L)).thenReturn(Optional.of(flight));
        when(tagRepo.findById(40L)).thenReturn(Optional.of(tag));
        when(flightMapper.toResponse(flight)).thenReturn(response);

        var result = service.removeTag(21L, 40L);

        assertThat(flight.getTags())
                .extracting(Tag::getId)
                .doesNotContain(40L);
        assertThat(tag.getFlights())
                .extracting(Flight::getId)
                .doesNotContain(21L);
        assertThat(result.id()).isEqualTo(21L);

        verify(flightRepo).findById(21L);
        verify(tagRepo).findById(40L);
        verify(flightMapper).toResponse(flight);
    }

    @Test
    void shouldDeleteFlight() {
        when(flightRepo.existsById(50L)).thenReturn(true);

        service.delete(50L);

        verify(flightRepo).deleteById(50L);
    }

    @Test
    void shouldThrowWhenDeletingNonexistentFlight() {
        when(flightRepo.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Flight 99 not found");
    }
}
