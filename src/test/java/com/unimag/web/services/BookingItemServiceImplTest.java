package com.unimag.web.services;

import com.unimag.web.api.dto.BookingDto.*;
import com.unimag.web.api.dto.FlightDto.*;
import com.unimag.web.domain.*;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.BookingItemRepository;
import com.unimag.web.repositories.BookingRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.services.impl.BookingItemServiceImpl;
import com.unimag.web.services.mapper.BookingMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingItemServiceImplTest {
    @Mock
    BookingItemRepository itemRepo;
    @Mock
    BookingRepository bookingRepo;
    @Mock
    FlightRepository flightRepo;
    @Mock
    BookingMapper bookingMapper;


    @InjectMocks
    BookingItemServiceImpl service;

    @Test
    void shouldAddItemAndMapToDto() {
        var booking = Booking.builder().id(1L).build();
        var flight = Flight.builder().id(2L).flightNumber("AV123").build();
        var req = new BookingItemCreateRequest("ECONOMY", "300.00", 1, 2L);

        var item = BookingItem.builder()
                .id(33L)
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.ECONOMY)
                .price(new BigDecimal("300.00"))
                .segmentOrder(1)
                .build();

        var response = new BookingItemResponse(
                33L,
                "ECONOMY",
                "300.00",
                1,
                new FlightResponse(2L, "AV123", OffsetDateTime.now(), OffsetDateTime.now().plusHours(1), null, null, null, Set.of())
        );

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(flightRepo.findById(2L)).thenReturn(Optional.of(flight));
        when(itemRepo.save(any())).thenReturn(item);

        try (MockedStatic<BookingMapper> mockedMapper = mockStatic(BookingMapper.class)) {
            mockedMapper.when(() -> bookingMapper.toItemResponse(item)).thenReturn(response);

            var out = service.addItem(1L, req);

            assertThat(out.id()).isEqualTo(33L);
            assertThat(out.cabin()).isEqualTo("ECONOMY");
            assertThat(out.flight().number()).isEqualTo("AV123");
        }
    }

    @Test
    void shouldListItemsMapping() {
        var booking = Booking.builder().id(1L).build();
        var flight = Flight.builder().id(2L).flightNumber("AV456").build();
        var item = BookingItem.builder().id(9L).booking(booking).flight(flight).build();

        var response = new BookingItemResponse(
                9L,
                "ECONOMY",
                "200.00",
                1,
                new FlightResponse(2L, "AV456", OffsetDateTime.now(), OffsetDateTime.now().plusHours(1), null, null, null, Set.of())
        );

        when(bookingRepo.findById(1L)).thenReturn(Optional.of(booking));
        when(itemRepo.findByBookingIdOrderBySegmentOrderAsc(1L)).thenReturn(List.of(item));

        try (MockedStatic<BookingMapper> mockedMapper = mockStatic(BookingMapper.class)) {
            mockedMapper.when(() -> bookingMapper.toItemResponse(item)).thenReturn(response);

            var list = service.listByBooking(1L);

            assertThat(list).hasSize(1);
            assertThat(list.get(0).id()).isEqualTo(9L);
            assertThat(list.get(0).flight().number()).isEqualTo("AV456");
        }
    }

    @Test
    void shouldThrowWhenBookingNotFoundOnAdd() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.empty());
        var req = new BookingItemCreateRequest("ECONOMY", "100.00", 1, 2L);

        assertThatThrownBy(() -> service.addItem(1L, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Booking 1 not found");
    }

    @Test
    void shouldThrowWhenFlightNotFoundOnAdd() {
        when(bookingRepo.findById(1L)).thenReturn(Optional.of(Booking.builder().id(1L).build()));
        when(flightRepo.findById(2L)).thenReturn(Optional.empty());
        var req = new BookingItemCreateRequest("ECONOMY", "100.00", 1, 2L);

        assertThatThrownBy(() -> service.addItem(1L, req))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Flight 2 not found");
    }
}
