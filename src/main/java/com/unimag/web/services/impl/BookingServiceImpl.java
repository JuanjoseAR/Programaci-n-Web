package com.unimag.web.services.impl;

import com.unimag.web.api.dto.BookingDto.*;
import com.unimag.web.domain.Booking;
import com.unimag.web.domain.BookingItem;
import com.unimag.web.domain.Flight;
import com.unimag.web.domain.Passenger;
import com.unimag.web.exception.NotFoundException;
import com.unimag.web.repositories.BookingRepository;
import com.unimag.web.repositories.FlightRepository;
import com.unimag.web.repositories.PassengerRepository;
import com.unimag.web.services.BookingService;
import com.unimag.web.services.mapper.BookingMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final FlightRepository flightRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingResponse create(BookingCreateRequest request) {
        Passenger passenger = passengerRepository.findById(request.passengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found with id " + request.passengerId()));

        Booking booking = bookingMapper.toEntity(request);
        booking.setCreatedAt(OffsetDateTime.now());
        booking.setPassenger(passenger);

        validateFlights(booking.getItems());

        Booking saved = bookingRepository.save(booking);
        return bookingMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse findById(Long id) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id " + id));
        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingResponse> findAll(Pageable pageable) {
        return bookingRepository.findAll(pageable)
                .map(bookingMapper::toResponse);
    }

    @Override
    public BookingResponse update(Long id, BookingCreateRequest request) {
        Booking booking = bookingRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new NotFoundException("Booking not found with id " + id));

        Passenger passenger = passengerRepository.findById(request.passengerId())
                .orElseThrow(() -> new NotFoundException("Passenger not found with id " + request.passengerId()));

        booking.setPassenger(passenger);

        booking.getItems().clear();
        List<BookingItem> newItems = request.items().stream()
                .map(bookingMapper::toItemEntity)
                .peek(item -> item.setBooking(booking))
                .toList();

        booking.getItems().addAll(newItems);

        Booking updated = bookingRepository.save(booking);
        return bookingMapper.toResponse(updated);
    }

    @Override
    public void delete(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new NotFoundException("Booking not found with id " + id);
        }
        bookingRepository.deleteById(id);
    }

    private void validateFlights(List<BookingItem> items) {
        for (BookingItem item : items) {
            Long flightId = item.getFlight().getId();
            flightRepository.findById(flightId)
                    .orElseThrow(() -> new NotFoundException("Flight not found with id " + flightId));
        }
    }
}

