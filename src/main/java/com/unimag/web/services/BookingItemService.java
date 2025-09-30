package com.unimag.web.services;

import com.unimag.web.api.dto.BookingDto.*;
import java.math.BigDecimal;
import java.util.List;

public interface BookingItemService {
    BookingItemResponse addItem(Long bookingId, BookingItemCreateRequest req);
    List<BookingItemResponse> listByBooking(Long bookingId);
    void removeItem(Long itemId);
    BigDecimal calculateTotal(Long bookingId);
    long countReservedSeats(Long flightId, String cabin);
}
