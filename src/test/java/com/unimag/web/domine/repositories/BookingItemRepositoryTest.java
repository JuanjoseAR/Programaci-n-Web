package com.unimag.web.domine.repositories;

import com.unimag.web.domain.*;
import com.unimag.web.repositories.BookingItemRepository;
import com.unimag.web.repositories.BookingRepository;
import com.unimag.web.repositories.FlightRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BookingItemRepositoryTest extends AbstractRepositoryIT {

    @Autowired
    private BookingItemRepository bookingItemRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Test
    @DisplayName("BookingItem: obtener ítems de una reserva ordenados por segmentOrder")
    void shouldFindByBookingIdOrderBySegmentOrderAsc() {

        Booking booking = bookingRepository.save(new Booking());

        Flight flight1 = flightRepository.save(new Flight());
        Flight flight2 = flightRepository.save(new Flight());

        BookingItem item1 = BookingItem.builder()
                .booking(booking)
                .flight(flight1)
                .cabin(Cabin.ECONOMY)
                .price(BigDecimal.valueOf(100))
                .segmentOrder(2)
                .build();

        BookingItem item2 = BookingItem.builder()
                .booking(booking)
                .flight(flight2)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(200))
                .segmentOrder(1)
                .build();

        bookingItemRepository.save(item1);
        bookingItemRepository.save(item2);


        List<BookingItem> items = bookingItemRepository.findByBookingIdOrderBySegmentOrderAsc(booking.getId());


        assertThat(items).hasSize(2);
        assertThat(items.get(0).getSegmentOrder()).isEqualTo(1);
        assertThat(items.get(1).getSegmentOrder()).isEqualTo(2);
    }

    @Test
    @DisplayName("BookingItem: calcular el total de una reserva")
    void shouldCalculateTotalByBookingId() {

        Booking booking = bookingRepository.save(new Booking());
        Flight flight = flightRepository.save(new Flight());

        bookingItemRepository.save(BookingItem.builder()
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .price(BigDecimal.valueOf(150))
                .segmentOrder(1)
                .build());

        bookingItemRepository.save(BookingItem.builder()
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(250))
                .segmentOrder(2)
                .build());


        BigDecimal total = bookingItemRepository.calculateTotalByBookingId(booking.getId());


        assertThat(total).isEqualByComparingTo("400");
    }

    @Test
    @DisplayName("BookingItem: contar asientos reservados por vuelo y cabina")
    void shouldCountReservedSeatsByFlightAndCabin() {

        Booking booking = bookingRepository.save(new Booking());
        Flight flight = flightRepository.save(new Flight());

        bookingItemRepository.save(BookingItem.builder()
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .price(BigDecimal.valueOf(100))
                .segmentOrder(1)
                .build());

        bookingItemRepository.save(BookingItem.builder()
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .price(BigDecimal.valueOf(120))
                .segmentOrder(2)
                .build());

        bookingItemRepository.save(BookingItem.builder()
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.BUSINESS)
                .price(BigDecimal.valueOf(200))
                .segmentOrder(3)
                .build());


        long economySeats = bookingItemRepository.countReservedSeatsByFlightAndCabin(flight.getId(), Cabin.ECONOMY);
        long businessSeats = bookingItemRepository.countReservedSeatsByFlightAndCabin(flight.getId(), Cabin.BUSINESS);


        assertThat(economySeats).isEqualTo(2);
        assertThat(businessSeats).isEqualTo(1);
    }
}
