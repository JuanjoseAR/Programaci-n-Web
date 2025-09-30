package com.unimag.web.services.mapper;

import com.unimag.web.domain.Booking;
import com.unimag.web.domain.BookingItem;
import com.unimag.web.domain.Cabin;
import com.unimag.web.domain.Flight;
import com.unimag.web.domain.Passenger;
import com.unimag.web.api.dto.BookingDto;
import com.unimag.web.api.dto.FlightDto;
import com.unimag.web.api.dto.PassengerDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    private final PassengerMapper passengerMapper;
    private final FlightMapper flightMapper;

    public BookingMapper(PassengerMapper passengerMapper, FlightMapper flightMapper) {
        this.passengerMapper = passengerMapper;
        this.flightMapper = flightMapper;
    }

    public BookingDto.BookingResponse toResponse(Booking booking) {
        if (booking == null) {
            return null;
        }

        PassengerDto.PassengerResponse passengerResponse = passengerMapper.toResponse(booking.getPassenger());
        List<BookingDto.BookingItemResponse> itemsResponse = (booking.getItems() != null)
                ? booking.getItems().stream().map(this::toItemResponse).collect(Collectors.toList())
                : Collections.emptyList();

        return new BookingDto.BookingResponse(
                booking.getId(),
                booking.getCreatedAt(),
                passengerResponse,
                itemsResponse
        );
    }

    public BookingDto.BookingItemResponse toItemResponse(BookingItem item) {
        if (item == null) {
            return null;
        }

        FlightDto.FlightResponse flightResponse = flightMapper.toResponse(item.getFlight());

        return new BookingDto.BookingItemResponse(
                item.getId(),
                item.getCabin().name(),
                item.getPrice().toString(),
                item.getSegmentOrder(),
                flightResponse
        );
    }

    public Booking toEntity(BookingDto.BookingCreateRequest request) {
        if (request == null) {
            return null;
        }

        Booking booking = new Booking();

        Passenger passenger = new Passenger();
        passenger.setId(request.passengerId());
        booking.setPassenger(passenger);

        if (request.items() != null) {
            List<BookingItem> items = request.items().stream()
                    .map(itemDto -> toItemEntity(itemDto, booking))
                    .collect(Collectors.toList());
            booking.setItems(items);
        } else {
            booking.setItems(Collections.emptyList());
        }

        return booking;
    }

    private BookingItem toItemEntity(BookingDto.BookingItemCreateRequest itemRequest, Booking booking) {
        if (itemRequest == null) {
            return null;
        }

        BookingItem item = new BookingItem();
        item.setCabin(Cabin.valueOf(itemRequest.cabin()));
        item.setPrice(new BigDecimal(itemRequest.price()));
        item.setSegmentOrder(itemRequest.segmentOrder());

        Flight flight = new Flight();
        flight.setId(itemRequest.flightId());
        item.setFlight(flight);

        item.setBooking(booking);

        return item;
    }
}