package com.unimag.web.api;

import com.unimag.web.api.dto.BookingDto.*;
import com.unimag.web.services.BookingItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
@Validated
public class BookingItemController {

    private final BookingItemService service;

    @PostMapping("/{bookingId}/items")
    public ResponseEntity<BookingItemResponse> addItem(@PathVariable Long bookingId,
                                                       @Valid @RequestBody BookingItemCreateRequest req,
                                                       UriComponentsBuilder uriBuilder) {
        var body = service.addItem(bookingId, req);
        var location = uriBuilder.path("/api/bookings/{bookingId}/items/{itemId}")
                .buildAndExpand(bookingId, body.id())
                .toUri();
        return ResponseEntity.created(location).body(body);
    }

    @GetMapping("/{bookingId}/items")
    public ResponseEntity<List<BookingItemResponse>> listByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(service.listByBooking(bookingId));
    }


    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@PathVariable Long itemId) {
        service.removeItem(itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{bookingId}/total")
    public ResponseEntity<BigDecimal> total(@PathVariable Long bookingId) {
        return ResponseEntity.ok(service.calculateTotal(bookingId));
    }

    @GetMapping("/items/flight/{flightId}/reserved")
    public ResponseEntity<Long> reservedSeats(@PathVariable Long flightId, @RequestParam String cabin) {
        return ResponseEntity.ok(service.countReservedSeats(flightId, cabin));
    }
}
