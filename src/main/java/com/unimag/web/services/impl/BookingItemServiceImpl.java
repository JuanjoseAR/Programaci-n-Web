package com.unimag.web.services.impl;

import com.unimag.web.api.dto.BookingDto.*;
import com.unimag.web.domain.Booking;
import com.unimag.web.domain.BookingItem;
import com.unimag.web.domain.Cabin;
import com.unimag.web.domain.Flight;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.BookingItemRepository;
import com.unimag.web.repositories.BookingRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.services.BookingItemService;
import com.unimag.web.services.mapper.BookingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingItemServiceImpl implements BookingItemService {

    private final BookingItemRepository itemRepo;
    private final BookingRepository bookingRepo;
    private final FlightRepository flightRepo;
    private final BookingMapper bookingMapper;

    @Override
    public BookingItemResponse addItem(Long bookingId, BookingItemCreateRequest req) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(bookingId)));

        Flight flight = flightRepo.findById(req.flightId())
                .orElseThrow(() -> new NotFoundException("Flight %d not found".formatted(req.flightId())));

        BookingItem item = BookingItem.builder()
                .booking(booking)
                .flight(flight)
                .cabin(Cabin.valueOf(req.cabin()))
                .price(new BigDecimal(req.price()))
                .segmentOrder(req.segmentOrder())
                .build();

        return bookingMapper.toItemResponse(itemRepo.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingItemResponse> listByBooking(Long bookingId) {
        Booking booking = bookingRepo.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Booking %d not found".formatted(bookingId)));

        return itemRepo.findByBookingIdOrderBySegmentOrderAsc(booking.getId())
                .stream()
                .map(bookingMapper::toItemResponse)
                .toList();
    }

    @Override
    public void removeItem(Long itemId) {
        if (!itemRepo.existsById(itemId)) {
            throw new NotFoundException("BookingItem %d not found".formatted(itemId));
        }
        itemRepo.deleteById(itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateTotal(Long bookingId) {
        if (!bookingRepo.existsById(bookingId)) {
            throw new NotFoundException("Booking %d not found".formatted(bookingId));
        }
        return itemRepo.calculateTotalByBookingId(bookingId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countReservedSeats(Long flightId, String cabin) {
        if (!flightRepo.existsById(flightId)) {
            throw new NotFoundException("Flight %d not found".formatted(flightId));
        }
        return itemRepo.countReservedSeatsByFlightAndCabin(flightId, Cabin.valueOf(cabin));
    }
}
