package com.unimag.web.domine.repositories;

import com.unimag.web.domain.*;
import com.unimag.web.repositories.AirlineRepository;
import com.unimag.web.repositories.AirportRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.repositories.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FlightRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private TagRepository tagRepository;

    private Airline createAirline(String code, String name) {
        Airline airline = new Airline();
        airline.setCode(code);
        airline.setName(name);
        return airlineRepository.save(airline);
    }

    private Airport createAirport(String code, String name, String city, String country) {
        Airport airport = new Airport();
        airport.setCode(code);
        airport.setName(name);
        airport.setCity(city);
        return airportRepository.save(airport);
    }

    @Test
    @DisplayName("Flight: buscar vuelos por nombre de aerolínea")
    void shouldFindFlightsByAirlineName() {

        Airline avianca = createAirline("AV", "Avianca");
        Airport origin = createAirport("SMR", "Simón Bolívar", "Santa Marta", "Colombia");
        Airport destination = createAirport("BOG", "El Dorado", "Bogotá", "Colombia");

        Flight flight = Flight.builder()
                .flightNumber("AV123")
                .airline(avianca)
                .origin(origin)
                .destination(destination)
                .departureTime(OffsetDateTime.now())
                .arrivalTime(OffsetDateTime.now().plusHours(1))
                .build();

        flightRepository.save(flight);


        Page<Flight> result = flightRepository.findByAirlineName("Avianca", PageRequest.of(0, 10));


        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFlightNumber()).isEqualTo("AV123");
    }

    @Test
    @DisplayName("Flight: buscar vuelos entre aeropuertos y rango de fechas")
    void shouldFindFlightsByOriginDestinationAndDateRange() {

        Airline avianca = createAirline("AV", "Avianca");
        Airport origin = createAirport("SMR", "Simón Bolívar", "Santa Marta", "Colombia");
        Airport destination = createAirport("BOG", "El Dorado", "Bogotá", "Colombia");

        OffsetDateTime departure = OffsetDateTime.now().plusDays(1);
        OffsetDateTime arrival = departure.plusHours(1);

        Flight flight = Flight.builder()
                .flightNumber("AV456")
                .airline(avianca)
                .origin(origin)
                .destination(destination)
                .departureTime(departure)
                .arrivalTime(arrival)
                .build();

        flightRepository.save(flight);

        Page<Flight> result = flightRepository.findByOriginCodeAndDestinationCodeAndDepartureTimeBetween(
                "SMR", "BOG",
                departure.minusHours(2),
                departure.plusHours(2),
                PageRequest.of(0, 10)
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getFlightNumber()).isEqualTo("AV456");
    }

    @Test
    @DisplayName("Flight: búsqueda dinámica con searchFlights (JOIN FETCH)")
    void shouldSearchFlightsWithFilters() {

        Airline avianca = createAirline("AV", "Avianca");
        Airport origin = createAirport("SMR", "Simón Bolívar", "Santa Marta", "Colombia");
        Airport destination = createAirport("BOG", "El Dorado", "Bogotá", "Colombia");

        OffsetDateTime departure = OffsetDateTime.now().plusDays(1);

        Flight flight = Flight.builder()
                .flightNumber("AV789")
                .airline(avianca)
                .origin(origin)
                .destination(destination)
                .departureTime(departure)
                .arrivalTime(departure.plusHours(1))
                .build();

        flightRepository.save(flight);


        List<Flight> results = flightRepository.searchFlights(
                "SMR", "BOG",
                departure.minusDays(1), departure.plusDays(1)
        );


        assertThat(results).hasSize(1);
        assertThat(results.get(0).getAirline().getName()).isEqualTo("Avianca");
        assertThat(results.get(0).getOrigin().getCode()).isEqualTo("SMR");
        assertThat(results.get(0).getDestination().getCode()).isEqualTo("BOG");
    }

    @Test
    @DisplayName("Flight: buscar vuelos que contengan todos los tags requeridos")
    void shouldFindFlightsByAllTags() {

        Airline avianca = createAirline("AV", "Avianca");
        Airport origin = createAirport("SMR", "Simón Bolívar", "Santa Marta", "Colombia");
        Airport destination = createAirport("BOG", "El Dorado", "Bogotá", "Colombia");

        OffsetDateTime departure = OffsetDateTime.now().plusDays(2);

        Tag promo = tagRepository.save(Tag.builder().name("PROMO").build());
        Tag direct = tagRepository.save(Tag.builder().name("DIRECT").build());

        Flight flight = Flight.builder()
                .flightNumber("AV999")
                .airline(avianca)
                .origin(origin)
                .destination(destination)
                .departureTime(departure)
                .arrivalTime(departure.plusHours(2))
                .build();

        flight.addTag(promo);
        flight.addTag(direct);

        flightRepository.save(flight);


        List<Flight> results = flightRepository.findByAllTags(
                List.of("PROMO", "DIRECT"),
                2
        );


        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFlightNumber()).isEqualTo("AV999");
    }
}
