package com.unimag.web.services;

import com.unimag.web.api.dto.BookingDto.*;
import com.unimag.web.api.dto.PassengerDto;
import com.unimag.web.domain.*;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.BookingRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.repositories.PassengerRepository;
import com.unimag.web.services.impl.BookingServiceImpl;
import com.unimag.web.services.mapper.BookingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock BookingRepository bookingRepository;
    @Mock PassengerRepository passengerRepository;
    @Mock FlightRepository flightRepository;

    @InjectMocks
    BookingServiceImpl service;

    @Test
    void shouldCreateBookingWithPassengerAndFlights() {
        var passenger = Passenger.builder().id(1L).fullName("Juan").email("j@test.com").build();
        var flight = Flight.builder().id(2L).build();

        var bookingItem = new BookingItem();
        bookingItem.setFlight(flight);

        var booking = Booking.builder().id(10L).createdAt(OffsetDateTime.now())
                .passenger(passenger).items(List.of(bookingItem)).build();
        bookingItem.setBooking(booking);

        var req = new BookingCreateRequest(1L, List.of(
                new BookingItemCreateRequest("ECONOMY", "100.00", 1, 2L)
        ));
        var response = new BookingResponse(10L, OffsetDateTime.now(),
                new PassengerDto.PassengerResponse(1L, "Juan", "j@test.com", null),
                List.of()
        );

        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(2L)).thenReturn(Optional.of(flight));
        when(bookingRepository.save(any())).thenReturn(booking);

        try (MockedStatic<BookingMapper> mocked = mockStatic(BookingMapper.class)) {
            mocked.when(() -> BookingMapper.toEntity(req)).thenReturn(booking);
            mocked.when(() -> BookingMapper.toResponse(booking)).thenReturn(response);

            var res = service.create(req);

            assertThat(res.id()).isEqualTo(10L);
            assertThat(res.passenger().email()).isEqualTo("j@test.com");
            verify(bookingRepository).save(any(Booking.class));
        }
    }

    @Test
    void shouldThrowWhenPassengerNotFoundOnCreate() {
        var req = new BookingCreateRequest(99L, List.of());
        when(passengerRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Passenger not found");
    }

    @Test
    void shouldFindBookingById() {
        var booking = Booking.builder().id(7L).createdAt(OffsetDateTime.now()).build();
        var response = new BookingResponse(7L, null, null, List.of());

        when(bookingRepository.findByIdWithDetails(7L)).thenReturn(Optional.of(booking));

        try (MockedStatic<BookingMapper> mocked = mockStatic(BookingMapper.class)) {
            mocked.when(() -> BookingMapper.toResponse(booking)).thenReturn(response);

            var res = service.findById(7L);

            assertThat(res.id()).isEqualTo(7L);
        }
    }

    @Test
    void shouldThrowWhenBookingNotFoundById() {
        when(bookingRepository.findByIdWithDetails(77L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(77L))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void shouldListBookingsPaged() {
        var booking = Booking.builder().id(5L).createdAt(OffsetDateTime.now()).build();
        var response = new BookingResponse(5L, null, null, List.of());
        var page = new PageImpl<>(List.of(booking));

        when(bookingRepository.findAll(PageRequest.of(0, 5))).thenReturn(page);

        try (MockedStatic<BookingMapper> mocked = mockStatic(BookingMapper.class)) {
            mocked.when(() -> BookingMapper.toResponse(booking)).thenReturn(response);

            var result = service.findAll(PageRequest.of(0, 5));

            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getContent().get(0).id()).isEqualTo(5L);
        }
    }

    @Test
    void shouldUpdateBookingReplacingItems() {
        var passenger = Passenger.builder().id(1L).email("p@test.com").build();
        var flight = Flight.builder().id(2L).build();

        var booking = Booking.builder().id(10L)
                .passenger(passenger)
                .items(new java.util.ArrayList<>())
                .build();

        var req = new BookingCreateRequest(1L, List.of(
                new BookingItemCreateRequest("BUSINESS", "200.00", 1, 2L)
        ));
        var response = new BookingResponse(10L, OffsetDateTime.now(),
                new PassengerDto.PassengerResponse(1L, "Juan", "p@test.com", null),
                List.of()
        );

        when(bookingRepository.findByIdWithDetails(10L)).thenReturn(Optional.of(booking));
        when(passengerRepository.findById(1L)).thenReturn(Optional.of(passenger));
        when(flightRepository.findById(2L)).thenReturn(Optional.of(flight));
        when(bookingRepository.save(any())).thenReturn(booking);

        try (MockedStatic<BookingMapper> mocked = mockStatic(BookingMapper.class)) {
            mocked.when(() -> BookingMapper.toResponse(booking)).thenReturn(response);

            var res = service.update(10L, req);

            assertThat(res.id()).isEqualTo(10L);
            verify(bookingRepository).save(booking);
            assertThat(booking.getItems()).hasSize(1);
            assertThat(booking.getItems().get(0).getPrice()).isEqualByComparingTo(BigDecimal.valueOf(200));
        }
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        when(bookingRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Booking not found");
    }

    @Test
    void shouldDeleteBookingWhenExists() {
        when(bookingRepository.existsById(5L)).thenReturn(true);

        service.delete(5L);

        verify(bookingRepository).deleteById(5L);
    }
}
