package com.unimag.web.domine.repositories;

import com.unimag.web.domain.*;
import com.unimag.web.repositories.BookingRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.repositories.PassengerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class BookingRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private FlightRepository flightRepository;



    @Test
    @DisplayName("Booking: buscar reservas por email de pasajero ignorando mayúsculas y ordenadas por fecha desc")
    void shouldFindBookingsByPassengerEmailIgnoreCaseOrderByCreatedAtDesc() {

        Passenger passenger = Passenger.builder()
                .fullName("Juan Perez")
                .email("juan@example.com")
                .build();
        passengerRepository.save(passenger);

        Booking booking1 = Booking.builder()
                .createdAt(OffsetDateTime.now().minusDays(1))
                .passenger(passenger)
                .build();
        Booking booking2 = Booking.builder()
                .createdAt(OffsetDateTime.now())
                .passenger(passenger)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);


        Page<Booking> page = bookingRepository.findByPassengerEmailIgnoreCaseOrderByCreatedAtDesc(
                "JUAN@example.com",
                PageRequest.of(0, 10)
        );


        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).getCreatedAt()).isAfter(page.getContent().get(1).getCreatedAt());
    }

    @Test
    @DisplayName("Booking: buscar una reserva con todos sus detalles")
    void shouldFindBookingByIdWithDetails() {

        Passenger passenger = Passenger.builder()
                .fullName("Maria Lopez")
                .email("maria@example.com")
                .build();
        passengerRepository.save(passenger);

        Flight flight = flightRepository.save(new Flight());

        Booking booking = Booking.builder()
                .createdAt(OffsetDateTime.now())
                .passenger(passenger)
                .build();
        booking = bookingRepository.save(booking);

        BookingItem item = BookingItem.builder()
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .price(BigDecimal.valueOf(300))
                .segmentOrder(1)
                .build();

        booking.getItems().add(item);
        bookingRepository.save(booking);


        Optional<Booking> found = bookingRepository.findByIdWithDetails(booking.getId());


        assertThat(found).isPresent();
        assertThat(found.get().getPassenger().getEmail()).isEqualTo("maria@example.com");
        assertThat(found.get().getItems()).hasSize(1);
        assertThat(found.get().getItems().get(0).getFlight()).isNotNull();
    }
}
