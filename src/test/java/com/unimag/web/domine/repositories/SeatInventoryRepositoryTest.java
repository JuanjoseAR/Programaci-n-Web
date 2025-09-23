package com.unimag.web.domine.repositories;

import com.unimag.web.domain.Airport;
import com.unimag.web.domain.Cabin;
import com.unimag.web.domain.Flight;
import com.unimag.web.domain.SeatInventory;
import com.unimag.web.repositories.AirportRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.repositories.SeatInventoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class SeatInventoryRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private SeatInventoryRepository seatInventoryRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Test
    @DisplayName("findByFlightIdAndCabin debe devolver el inventario si existe")
    void testFindByFlightIdAndCabin_found() {
        Airport airportOrigin = Airport.builder()
                .code("SMR")
                .build();
        Airport airportDestination = Airport.builder()
                .code("BOG")
                .build();
        airportRepository.save(airportOrigin);
        airportRepository.save(airportDestination);

        Flight flight = Flight.builder()
                .flightNumber("UN123")
                .origin(airportOrigin)
                .destination(airportDestination)
                .build();
        flightRepository.save(flight);

        SeatInventory si = SeatInventory.builder()
                .cabin(Cabin.ECONOMY)
                .totalSeats(100)
                .availableSeats(80)
                .flight(flight)
                .build();
        seatInventoryRepository.save(si);

        var result = seatInventoryRepository.findByFlightIdAndCabin(flight.getId(), Cabin.ECONOMY);

        assertThat(result).isPresent();
        assertThat(result.get().getAvailableSeats()).isEqualTo(80);
    }

    @Test
    @DisplayName("findByFlightIdAndCabin debe devolver vacío si no existe")
    void testFindByFlightIdAndCabin_notFound() {
        var result = seatInventoryRepository.findByFlightIdAndCabin(999L, Cabin.BUSINESS);
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("hasAvailableSeats debe devolver true si hay suficientes asientos")
    void testHasAvailableSeats_true() {
        Airport airportOrigin = Airport.builder()
                .code("SMR")
                .build();
        Airport airportDestination = Airport.builder()
                .code("MDE")
                .build();
        airportRepository.save(airportOrigin);
        airportRepository.save(airportDestination);

        Flight flight = Flight.builder()
                .flightNumber("UN456")
                .origin(airportOrigin)
                .destination(airportDestination)
                .build();
        flightRepository.save(flight);

        SeatInventory si = SeatInventory.builder()
                .cabin(Cabin.BUSINESS)
                .totalSeats(50)
                .availableSeats(10)
                .flight(flight)
                .build();
        seatInventoryRepository.save(si);

        boolean result = seatInventoryRepository.hasAvailableSeats(flight.getId(), Cabin.BUSINESS, 5);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("hasAvailableSeats debe devolver false si no hay suficientes asientos")
    void testHasAvailableSeats_false() {
        Airport airportOrigin = Airport.builder()
                .code("CTG")
                .build();
        Airport airportDestination = Airport.builder()
                .code("SMR")
                .build();
        airportRepository.save(airportOrigin);
        airportRepository.save(airportDestination);

        Flight flight = Flight.builder()
                .flightNumber("UN789")
                .origin(airportOrigin)
                .destination(airportDestination)
                .build();
        flightRepository.save(flight);

        SeatInventory si = SeatInventory.builder()
                .cabin(Cabin.PREMIUN)
                .totalSeats(20)
                .availableSeats(2)
                .flight(flight)
                .build();
        seatInventoryRepository.save(si);

        boolean result = seatInventoryRepository.hasAvailableSeats(flight.getId(), Cabin.PREMIUN, 5);

        assertThat(result).isFalse();
    }
}
